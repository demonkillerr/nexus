#pragma once

#include <string>

struct HttpResponse {
  int statusCode{0};
  std::string body;
};

class HttpClient {
public:
  HttpResponse get(const std::string& url);
  HttpResponse postJson(const std::string& url, const std::string& jsonBody);
};
