package com.jobscheduler.worker_service.repo;

import com.jobscheduler.worker_service.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<Job, String> {
    List<Job> findByOwnerId(String email);

}
