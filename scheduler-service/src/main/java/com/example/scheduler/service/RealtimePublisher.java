package com.example.scheduler.service;

import com.example.scheduler.dto.JobDto;
import com.example.scheduler.dto.WorkerDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RealtimePublisher {
  private final SimpMessagingTemplate messaging;

  public RealtimePublisher(SimpMessagingTemplate messaging) {
    this.messaging = messaging;
  }

  public void publishJob(JobDto job) {
    messaging.convertAndSend("/topic/jobs", job);
    messaging.convertAndSend("/topic/jobs/" + job.getId(), job);
  }

  public void publishWorker(WorkerDto worker) {
    messaging.convertAndSend("/topic/workers", worker);
  }
}
