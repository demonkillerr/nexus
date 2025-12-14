package com.example.scheduler.repository;

import com.example.scheduler.domain.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, String> {
}
