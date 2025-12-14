package com.example.scheduler.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterWorkerRequest {
  @NotBlank
  private String workerId;

  private String capabilities;

  public String getWorkerId() {
    return workerId;
  }

  public void setWorkerId(String workerId) {
    this.workerId = workerId;
  }

  public String getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(String capabilities) {
    this.capabilities = capabilities;
  }
}
