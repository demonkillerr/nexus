package com.example.scheduler.dto;

import com.example.scheduler.domain.WorkerStatus;
import java.time.Instant;

public class WorkerDto {
  private String id;
  private WorkerStatus status;
  private Instant lastHeartbeat;
  private String capabilities;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public WorkerStatus getStatus() {
    return status;
  }

  public void setStatus(WorkerStatus status) {
    this.status = status;
  }

  public Instant getLastHeartbeat() {
    return lastHeartbeat;
  }

  public void setLastHeartbeat(Instant lastHeartbeat) {
    this.lastHeartbeat = lastHeartbeat;
  }

  public String getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(String capabilities) {
    this.capabilities = capabilities;
  }
}
