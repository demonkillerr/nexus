package com.example.scheduler.controller;

import com.example.scheduler.dto.JobDto;
import com.example.scheduler.dto.JobRequest;
import com.example.scheduler.service.JobService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
  private final JobService jobService;

  public JobController(JobService jobService) {
    this.jobService = jobService;
  }

  @PostMapping
  public JobDto submit(@Valid @RequestBody JobRequest request) {
    return jobService.submit(request.getParameters());
  }

  @GetMapping
  public List<JobDto> list() {
    return jobService.list();
  }

  @GetMapping("/{id}")
  public JobDto get(@PathVariable UUID id) {
    return jobService.get(id);
  }
}
