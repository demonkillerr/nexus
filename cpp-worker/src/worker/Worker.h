#pragma once

#include "messaging/HttpClient.h"
#include "worker/JobExecutor.h"

#include <atomic>
#include <string>

class Worker {
public:
  Worker(std::string workerId, std::string schedulerBaseUrl);
  void runForever();

private:
  void registerWorker();
  void sendHeartbeat();
  void pollAndExecute();

  std::string workerId_;
  std::string schedulerBaseUrl_;
  HttpClient http_;
  JobExecutor executor_;
  std::atomic<bool> running_{true};
};
