#include "worker/Worker.h"

#include <iostream>
#include <string>

static std::string getArg(int argc, char** argv, const std::string& name, const std::string& def) {
  for (int i = 1; i < argc - 1; i++) {
    if (std::string(argv[i]) == name) {
      return argv[i + 1];
    }
  }
  return def;
}

int main(int argc, char** argv) {
  const std::string workerId = getArg(argc, argv, "--worker-id", "worker-01");
  const std::string scheduler = getArg(argc, argv, "--scheduler", "http://localhost:8080");

  try {
    Worker worker(workerId, scheduler);
    worker.runForever();
  } catch (const std::exception& ex) {
    std::cerr << "fatal: " << ex.what() << "\n";
    return 1;
  }

  return 0;
}
