#include "messaging/HttpClient.h"

#include <cstdio>
#include <stdexcept>
#include <string>

static std::string readAll(FILE* f) {
  std::string out;
  char buf[4096];
  while (true) {
    size_t n = std::fread(buf, 1, sizeof(buf), f);
    if (n > 0) out.append(buf, n);
    if (n < sizeof(buf)) break;
  }
  return out;
}

static HttpResponse runCurl(const std::string& cmd) {
  FILE* f = _popen(cmd.c_str(), "r");
  if (!f) throw std::runtime_error("failed to run curl");
  std::string out = readAll(f);
  int rc = _pclose(f);
  (void)rc;

  // Our command prints: <body>\nHTTPSTATUS:<code>
  auto pos = out.rfind("HTTPSTATUS:");
  if (pos == std::string::npos) {
    throw std::runtime_error("curl output missing HTTPSTATUS");
  }
  std::string body = out.substr(0, pos);
  std::string codeStr = out.substr(pos + 11);
  int code = std::stoi(codeStr);
  return HttpResponse{code, body};
}

HttpResponse HttpClient::get(const std::string& url) {
  std::string cmd = "curl -s -o - -w \"HTTPSTATUS:%{http_code}\" \"" + url + "\"";
  return runCurl(cmd);
}

HttpResponse HttpClient::postJson(const std::string& url, const std::string& jsonBody) {
  // Use single quotes to avoid most escaping issues; ok for Windows curl.
  std::string cmd = "curl -s -o - -w \"HTTPSTATUS:%{http_code}\" -H \"Content-Type: application/json\" -X POST --data '" + jsonBody + "' \"" + url + "\"";
  return runCurl(cmd);
}
