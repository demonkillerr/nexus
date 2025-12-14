#include "worker/JobExecutor.h"

#include <chrono>
#include <thread>

static int findJsonInt(const std::string& json, const std::string& key, int defValue) {
  const std::string pattern = "\"" + key + "\"";
  auto pos = json.find(pattern);
  if (pos == std::string::npos) return defValue;
  pos = json.find(':', pos);
  if (pos == std::string::npos) return defValue;
  pos++;
  while (pos < json.size() && (json[pos] == ' ' || json[pos] == '\t')) pos++;
  bool neg = false;
  if (pos < json.size() && json[pos] == '-') { neg = true; pos++; }
  int value = 0;
  while (pos < json.size() && json[pos] >= '0' && json[pos] <= '9') {
    value = value * 10 + (json[pos] - '0');
    pos++;
  }
  return neg ? -value : value;
}

void JobExecutor::executeSimulated(const std::string& commandJson, const std::function<void(double)>& onProgress) {
  const int steps = findJsonInt(commandJson, "steps", 120);
  const int sleepMs = findJsonInt(commandJson, "sleepMs", 50);
  for (int i = 1; i <= steps; i++) {
    std::this_thread::sleep_for(std::chrono::milliseconds(sleepMs));
    double percent = (100.0 * i) / steps;
    onProgress(percent);
  }
}
