package com.example.scheduler.service;

import com.example.scheduler.domain.Job;
import com.example.scheduler.domain.Worker;
import com.example.scheduler.dto.JobDto;
import com.example.scheduler.dto.WorkerDto;

public final class Dtos {
  private Dtos() {
  }

  public static JobDto toDto(Job job) {
    JobDto dto = new JobDto();
    dto.setId(job.getId());
    dto.setStatus(job.getStatus());
    dto.setCreatedAt(job.getCreatedAt());
    dto.setCompletedAt(job.getCompletedAt());
    dto.setAssignedWorkerId(job.getAssignedWorkerId());
    dto.setLastPercent(job.getLastPercent());
    dto.setParameters(job.getParameters());
    return dto;
  }

  public static WorkerDto toDto(Worker worker) {
    WorkerDto dto = new WorkerDto();
    dto.setId(worker.getId());
    dto.setStatus(worker.getStatus());
    dto.setLastHeartbeat(worker.getLastHeartbeat());
    dto.setCapabilities(worker.getCapabilities());
    return dto;
  }
}
