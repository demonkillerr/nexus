#pragma once

#include <functional>
#include <string>

class JobExecutor {
public:
  void executeSimulated(const std::string& commandJson, const std::function<void(double)>& onProgress);
};
