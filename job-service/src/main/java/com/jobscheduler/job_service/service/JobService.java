package com.jobscheduler.job_service.service;

import com.jobscheduler.job_service.dto.JobEvent;
import com.jobscheduler.job_service.dto.JobRequest;
import com.jobscheduler.job_service.dto.JobResponse;
import com.jobscheduler.job_service.kafka.KafkaProducer;
import com.jobscheduler.job_service.model.Job;
import com.jobscheduler.job_service.model.JobStatus;
import com.jobscheduler.job_service.model.JobType;
import com.jobscheduler.job_service.repo.JobRepo;
import com.jobscheduler.job_service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private KafkaProducer kafkaProducer;


    public JobResponse submitJob(JobRequest request, String authHeader) {
        String email = jwtUtil.extractUsername(
                authHeader.replace("Bearer ", "")
        );

        Job job = new Job();
        job.setJobType(JobType.valueOf(request.type()));
        job.setPayload(request.payload());
        job.setRetryCount(0);
        job.setOwnerId(email);
        job.setCreatedAt(LocalDateTime.now());
        job.setJobStatus(JobStatus.PENDING);
        jobRepo.save(job);

        JobEvent jobEvent = new JobEvent(
                job.getId(),
                job.getJobType().toString(),
                job.getPayload(),
                job.getOwnerId(),
                job.getRetryCount(),
                ""
        );
        kafkaProducer.publishJob(jobEvent);

        return new JobResponse(
                job.getId(),
                job.getJobType().toString(),
                job.getJobStatus().toString(),
                job.getOwnerId(),
                job.getCreatedAt()
        );
    }


    public List<Job> getMyJobs(String authHeader){
        String email = jwtUtil.extractUsername(
                authHeader.replace("Bearer ", "")
        );
        return jobRepo.findByOwnerId(email);
    }

    public Job getJobById(String id, String authHeader){
        String email = jwtUtil.extractUsername(
                authHeader.replace("Bearer ", "")
        );

        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if(!job.getOwnerId().equals(email)){
            throw new RuntimeException("Unauthorized");
        }
        return job;
    }
}
