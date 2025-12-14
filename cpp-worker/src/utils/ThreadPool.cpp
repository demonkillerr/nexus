#include "utils/ThreadPool.h"

ThreadPool::ThreadPool(size_t threads) {
  threads_.reserve(threads);
  for (size_t i = 0; i < threads; i++) {
    threads_.emplace_back([this]() { workerLoop(); });
  }
}

ThreadPool::~ThreadPool() {
  {
    std::lock_guard<std::mutex> lock(mutex_);
    stop_ = true;
  }
  cv_.notify_all();
  for (auto& t : threads_) {
    if (t.joinable()) t.join();
  }
}

void ThreadPool::enqueue(std::function<void()> fn) {
  {
    std::lock_guard<std::mutex> lock(mutex_);
    queue_.push(std::move(fn));
  }
  cv_.notify_one();
}

void ThreadPool::workerLoop() {
  while (true) {
    std::function<void()> task;
    {
      std::unique_lock<std::mutex> lock(mutex_);
      cv_.wait(lock, [this]() { return stop_ || !queue_.empty(); });
      if (stop_ && queue_.empty()) {
        return;
      }
      task = std::move(queue_.front());
      queue_.pop();
    }
    task();
  }
}
