package com.example.scheduler.service;

import com.example.scheduler.domain.Worker;
import com.example.scheduler.domain.WorkerStatus;
import com.example.scheduler.dto.RegisterWorkerRequest;
import com.example.scheduler.dto.WorkerDto;
import com.example.scheduler.repository.WorkerRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkerService {
  private final WorkerRepository workers;
  private final RealtimePublisher realtime;
  private final Duration heartbeatTimeout;

  public WorkerService(
      WorkerRepository workers,
      RealtimePublisher realtime,
      @Value("${nexus.heartbeat-timeout-seconds}") long heartbeatTimeoutSeconds
  ) {
    this.workers = workers;
    this.realtime = realtime;
    this.heartbeatTimeout = Duration.ofSeconds(heartbeatTimeoutSeconds);
  }

  @Transactional
  public WorkerDto register(RegisterWorkerRequest request) {
    Worker worker = workers.findById(request.getWorkerId()).orElseGet(Worker::new);
    worker.setId(request.getWorkerId());
    worker.setCapabilities(request.getCapabilities());
    worker.setLastHeartbeat(Instant.now());
    worker.setStatus(WorkerStatus.UP);
    Worker saved = workers.save(worker);
    WorkerDto dto = Dtos.toDto(saved);
    realtime.publishWorker(dto);
    return dto;
  }

  @Transactional
  public WorkerDto heartbeat(String workerId) {
    Worker worker = workers.findById(workerId).orElseThrow();
    worker.setLastHeartbeat(Instant.now());
    if (worker.getStatus() != WorkerStatus.UP) {
      worker.setStatus(WorkerStatus.UP);
    }
    Worker saved = workers.save(worker);
    WorkerDto dto = Dtos.toDto(saved);
    realtime.publishWorker(dto);
    return dto;
  }

  public List<WorkerDto> list() {
    return workers.findAll().stream().map(Dtos::toDto).toList();
  }

  @Scheduled(fixedDelay = 2000)
  @Transactional
  public void markDownIfNoHeartbeat() {
    Instant now = Instant.now();
    for (Worker worker : workers.findAll()) {
      Instant last = worker.getLastHeartbeat();
      boolean stale = last == null || Duration.between(last, now).compareTo(heartbeatTimeout) > 0;
      if (stale && worker.getStatus() != WorkerStatus.DOWN) {
        worker.setStatus(WorkerStatus.DOWN);
        Worker saved = workers.save(worker);
        realtime.publishWorker(Dtos.toDto(saved));
      }
    }
  }
}
