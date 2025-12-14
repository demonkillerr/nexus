package com.example.scheduler.service;

import com.example.scheduler.domain.Job;
import com.example.scheduler.domain.JobStatus;
import com.example.scheduler.dto.JobDto;
import com.example.scheduler.repository.JobRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobService {
  private final JobRepository jobs;
  private final ObjectMapper objectMapper;
  private final RealtimePublisher realtime;

  public JobService(JobRepository jobs, ObjectMapper objectMapper, RealtimePublisher realtime) {
    this.jobs = jobs;
    this.objectMapper = objectMapper;
    this.realtime = realtime;
  }

  @Transactional
  public JobDto submit(Map<String, Object> parameters) {
    Job job = new Job();
    job.setId(UUID.randomUUID());
    job.setStatus(JobStatus.QUEUED);
    job.setCreatedAt(Instant.now());
    job.setParameters(toJson(parameters));
    job.setLastPercent(0.0);
    Job saved = jobs.save(job);
    JobDto dto = Dtos.toDto(saved);
    realtime.publishJob(dto);
    return dto;
  }

  public List<JobDto> list() {
    return jobs.findTop100ByOrderByCreatedAtDesc().stream().map(Dtos::toDto).toList();
  }

  public JobDto get(UUID id) {
    return jobs.findById(id).map(Dtos::toDto).orElseThrow();
  }

  private String toJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Invalid parameters", e);
    }
  }
}
