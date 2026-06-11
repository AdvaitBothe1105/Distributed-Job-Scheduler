package com.jobscheduler.admin_service.repo;

import com.jobscheduler.admin_service.model.Job;
import com.jobscheduler.admin_service.model.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepo extends JpaRepository<Job, String> {

    List<Job> findByOwnerId(String ownerId);

    List<Job> findByJobStatus(JobStatus status);

    long countByJobStatus(JobStatus status);
}
