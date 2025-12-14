package com.example.scheduler.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "workers")
public class Worker {
  @Id
  private String id;

  @Enumerated(EnumType.STRING)
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
