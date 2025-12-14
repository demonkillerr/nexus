package com.example.scheduler.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class JobRequest {
  @NotNull
  private Map<String, Object> parameters;

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }
}
