package com.example.scheduler.service;

import com.example.scheduler.domain.Job;
import com.example.scheduler.domain.JobExecution;
import com.example.scheduler.domain.JobStatus;
import com.example.scheduler.dto.WorkerEventRequest;
import com.example.scheduler.repository.JobExecutionRepository;
import com.example.scheduler.repository.JobRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkerEventService {
  private final JobRepository jobs;
  private final JobExecutionRepository executions;
  private final WorkerService workerService;
  private final RealtimePublisher realtime;

  public WorkerEventService(JobRepository jobs, JobExecutionRepository executions, WorkerService workerService, RealtimePublisher realtime) {
    this.jobs = jobs;
    this.executions = executions;
    this.workerService = workerService;
    this.realtime = realtime;
  }

  @Transactional
  public void ingest(String workerId, WorkerEventRequest request) {
    String type = request.getType().toUpperCase();
    if (type.equals("HEARTBEAT")) {
      workerService.heartbeat(workerId);
      return;
    }

    UUID jobId = request.getJobId();
    if (jobId == null) {
      return;
    }

    Job job = jobs.findById(jobId).orElseThrow();
    if (type.equals("PROGRESS")) {
      Object percent = request.getPayload().get("percent");
      if (percent instanceof Number n) {
        job.setLastPercent(n.doubleValue());
      }
      jobs.save(job);
      realtime.publishJob(Dtos.toDto(job));
      return;
    }

    if (type.equals("STATUS")) {
      Object status = request.getPayload().get("status");
      if (status instanceof String s) {
        JobStatus newStatus = JobStatus.valueOf(s.toUpperCase());
        job.setStatus(newStatus);
        if (newStatus == JobStatus.COMPLETED || newStatus == JobStatus.FAILED || newStatus == JobStatus.CANCELED) {
          job.setCompletedAt(Instant.now());
          closeExecution(job.getId());
        }
        jobs.save(job);
        realtime.publishJob(Dtos.toDto(job));
      }
    }
  }

  private void closeExecution(UUID jobId) {
    Optional<JobExecution> latest = executions.findAll().stream()
        .filter(e -> e.getJobId().equals(jobId))
        .filter(e -> e.getEndTime() == null)
        .findFirst();
    if (latest.isPresent()) {
      JobExecution exec = latest.get();
      exec.setEndTime(Instant.now());
      executions.save(exec);
    }
  }
}
