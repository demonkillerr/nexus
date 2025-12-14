package com.example.scheduler.repository;

import com.example.scheduler.domain.JobExecution;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobExecutionRepository extends JpaRepository<JobExecution, UUID> {
}
