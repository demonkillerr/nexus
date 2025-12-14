package com.example.scheduler.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jobs")
public class Job {
  @Id
  private UUID id;

  @Enumerated(EnumType.STRING)
  private JobStatus status;

  private Instant createdAt;
  private Instant completedAt;

  @Column(columnDefinition = "text")
  private String parameters;

  private String assignedWorkerId;

  private Double lastPercent;

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

  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) {
    this.parameters = parameters;
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
}
