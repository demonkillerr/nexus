package com.example.scheduler.repository;

import com.example.scheduler.domain.Job;
import com.example.scheduler.domain.JobStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;

public interface JobRepository extends JpaRepository<Job, UUID> {
  List<Job> findTop100ByOrderByCreatedAtDesc();

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select j from Job j where j.status = :status order by j.createdAt asc")
  List<Job> findQueuedForUpdate(JobStatus status);
}
