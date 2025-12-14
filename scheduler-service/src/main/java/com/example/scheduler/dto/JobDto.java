package com.example.scheduler.dto;

import com.example.scheduler.domain.JobStatus;
import java.time.Instant;
import java.util.UUID;

public class JobDto {
  private UUID id;
  private JobStatus status;
  private Instant createdAt;
  private Instant completedAt;
  private String assignedWorkerId;
  private Double lastPercent;
  private String parameters;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public JobStatus getStatus() {
    return status;
  }

  public void setStatus(JobStatus status) {
    this.status = status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(Instant completedAt) {
    this.completedAt = completedAt;
  }

  public String getAssignedWorkerId() {
    return assignedWorkerId;
  }

  public void setAssignedWorkerId(String assignedWorkerId) {
    this.assignedWorkerId = assignedWorkerId;
  }

  public Double getLastPercent() {
    return lastPercent;
  }

  public void setLastPercent(Double lastPercent) {
    this.lastPercent = lastPercent;
  }

  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
  }
}
