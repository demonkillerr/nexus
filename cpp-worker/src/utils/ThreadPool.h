#pragma once

#include <condition_variable>
#include <functional>
#include <mutex>
#include <queue>
#include <thread>
#include <vector>

class ThreadPool {
public:
  explicit ThreadPool(size_t threads);
  ~ThreadPool();

  void enqueue(std::function<void()> fn);

private:
  void workerLoop();

  std::mutex mutex_;
  std::condition_variable cv_;
  std::queue<std::function<void()>> queue_;
  bool stop_{false};
  std::vector<std::thread> threads_;
};
