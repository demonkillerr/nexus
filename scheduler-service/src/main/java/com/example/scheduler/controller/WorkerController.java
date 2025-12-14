package com.example.scheduler.controller;

import com.example.scheduler.dto.RegisterWorkerRequest;
import com.example.scheduler.dto.WorkerCommandDto;
import com.example.scheduler.dto.WorkerDto;
import com.example.scheduler.dto.WorkerEventRequest;
import com.example.scheduler.service.SchedulerService;
import com.example.scheduler.service.WorkerEventService;
import com.example.scheduler.service.WorkerService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workers")
public class WorkerController {
  private final WorkerService workerService;
  private final SchedulerService schedulerService;
  private final WorkerEventService eventService;

  public WorkerController(WorkerService workerService, SchedulerService schedulerService, WorkerEventService eventService) {
    this.workerService = workerService;
    this.schedulerService = schedulerService;
    this.eventService = eventService;
  }

  @PostMapping("/register")
  public WorkerDto register(@Valid @RequestBody RegisterWorkerRequest request) {
    return workerService.register(request);
  }

  @GetMapping
  public List<WorkerDto> list() {
    return workerService.list();
  }

  @GetMapping("/{workerId}/next-command")
  public ResponseEntity<WorkerCommandDto> nextCommand(@PathVariable String workerId) {
    return schedulerService.pollNextCommand(workerId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.noContent().build());
  }

  @PostMapping("/{workerId}/events")
  public ResponseEntity<Void> events(@PathVariable String workerId, @Valid @RequestBody WorkerEventRequest request) {
    eventService.ingest(workerId, request);
    return ResponseEntity.accepted().build();
  }
}
