#include "worker/Worker.h"

#include <chrono>
#include <iostream>
#include <thread>

static std::string jsonEscape(const std::string& s) {
  std::string out;
  out.reserve(s.size() + 8);
  for (char c : s) {
    if (c == '\\' || c == '"') {
      out.push_back('\\');
    }
    out.push_back(c);
  }
  return out;
}

static std::string findJsonString(const std::string& json, const std::string& key) {
  const std::string pattern = "\"" + key + "\"";
  auto pos = json.find(pattern);
  if (pos == std::string::npos) return "";
  pos = json.find(':', pos);
  if (pos == std::string::npos) return "";
  pos = json.find('"', pos);
  if (pos == std::string::npos) return "";
  auto end = json.find('"', pos + 1);
  if (end == std::string::npos) return "";
  return json.substr(pos + 1, end - pos - 1);
}

Worker::Worker(std::string workerId, std::string schedulerBaseUrl)
    : workerId_(std::move(workerId)), schedulerBaseUrl_(std::move(schedulerBaseUrl)) {
}

void Worker::runForever() {
  registerWorker();

  std::thread heartbeatThread([this]() {
    while (running_) {
      try {
        sendHeartbeat();
      } catch (const std::exception& ex) {
        std::cerr << "heartbeat error: " << ex.what() << "\n";
      }
      std::this_thread::sleep_for(std::chrono::seconds(2));
    }
  });

  while (running_) {
    try {
      pollAndExecute();
    } catch (const std::exception& ex) {
      std::cerr << "poll/execute error: " << ex.what() << "\n";
      std::this_thread::sleep_for(std::chrono::seconds(1));
    }
  }

  heartbeatThread.join();
}

void Worker::registerWorker() {
  std::string url = schedulerBaseUrl_ + "/api/workers/register";
  std::string body = "{\"workerId\":\"" + jsonEscape(workerId_) + "\",\"capabilities\":\"CPU\"}";
  http_.postJson(url, body);
  std::cout << "registered worker " << workerId_ << "\n";
}

void Worker::sendHeartbeat() {
  std::string url = schedulerBaseUrl_ + "/api/workers/" + workerId_ + "/events";
  std::string body = "{\"type\":\"HEARTBEAT\",\"payload\":{}}";
  http_.postJson(url, body);
}

void Worker::pollAndExecute() {
  const std::string url = schedulerBaseUrl_ + "/api/workers/" + workerId_ + "/next-command";
  HttpResponse res = http_.get(url);
  if (res.statusCode == 204) {
    std::this_thread::sleep_for(std::chrono::milliseconds(500));
    return;
  }
  if (res.statusCode < 200 || res.statusCode >= 300) {
    throw std::runtime_error("next-command HTTP " + std::to_string(res.statusCode));
  }

  const std::string jobId = findJsonString(res.body, "jobId");
  if (jobId.empty()) {
    throw std::runtime_error("could not parse jobId");
  }

  std::cout << "starting job " << jobId << "\n";

  auto progressCallback = [this, jobId](double percent) {
    const std::string url2 = schedulerBaseUrl_ + "/api/workers/" + workerId_ + "/events";
    std::string body = "{\"type\":\"PROGRESS\",\"jobId\":\"" + jobId + "\",\"payload\":{\"percent\":" + std::to_string(percent) + "}}";
    http_.postJson(url2, body);
  };

  auto statusCallback = [this, jobId](const std::string& status) {
    const std::string url2 = schedulerBaseUrl_ + "/api/workers/" + workerId_ + "/events";
    std::string body = "{\"type\":\"STATUS\",\"jobId\":\"" + jobId + "\",\"payload\":{\"status\":\"" + jsonEscape(status) + "\"}}";
    http_.postJson(url2, body);
  };

  try {
    statusCallback("RUNNING");
    executor_.executeSimulated(res.body, progressCallback);
    statusCallback("COMPLETED");
  } catch (...) {
    statusCallback("FAILED");
    throw;
  }
}
