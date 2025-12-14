package com.example.scheduler.service;

import com.example.scheduler.domain.Job;
import com.example.scheduler.domain.JobExecution;
import com.example.scheduler.domain.JobStatus;
import com.example.scheduler.dto.WorkerCommandDto;
import com.example.scheduler.repository.JobExecutionRepository;
import com.example.scheduler.repository.JobRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchedulerService {
  private final JobRepository jobs;
  private final JobExecutionRepository executions;
  private final RealtimePublisher realtime;

  public SchedulerService(JobRepository jobs, JobExecutionRepository executions, RealtimePublisher realtime) {
    this.jobs = jobs;
    this.executions = executions;
    this.realtime = realtime;
  }

  @Transactional
  public Optional<WorkerCommandDto> pollNextCommand(String workerId) {
    List<Job> queued = jobs.findQueuedForUpdate(JobStatus.QUEUED);
    if (queued.isEmpty()) {
      return Optional.empty();
    }

    Job job = queued.getFirst();
    job.setStatus(JobStatus.RUNNING);
    job.setAssignedWorkerId(workerId);
    jobs.save(job);

    JobExecution exec = new JobExecution();
    exec.setId(UUID.randomUUID());
    exec.setJobId(job.getId());
    exec.setWorkerId(workerId);
    exec.setStartTime(Instant.now());
    executions.save(exec);

    realtime.publishJob(Dtos.toDto(job));

    WorkerCommandDto cmd = new WorkerCommandDto();
    cmd.setCommand("START");
    cmd.setJobId(job.getId());
    cmd.setParameters(job.getParameters());
    return Optional.of(cmd);
  }
}
