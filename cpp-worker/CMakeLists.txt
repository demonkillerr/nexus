cmake_minimum_required(VERSION 3.20)
project(cpp_worker LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_executable(cpp-worker
  src/main.cpp
  src/worker/Worker.cpp
  src/worker/JobExecutor.cpp
  src/utils/ThreadPool.cpp
  src/messaging/HttpClient.cpp
)

target_include_directories(cpp-worker PRIVATE src)
