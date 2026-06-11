package com.jobscheduler.admin_service.service;

import com.jobscheduler.admin_service.dto.JobEvent;
import com.jobscheduler.admin_service.dto.StatsResponse;
import com.jobscheduler.admin_service.kafka.KafkaProducer;
import com.jobscheduler.admin_service.model.Job;
import com.jobscheduler.admin_service.model.JobStatus;
import com.jobscheduler.admin_service.repo.JobRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private KafkaProducer kafkaProducer;

    public List<Job> getAllJobs(){
        return jobRepo.findAll();
    }

    public List<Job> getJobByStatus(String status){
        return jobRepo.findByJobStatus(JobStatus.valueOf(status));
    }

    public List<Job> getDLQJobs(){
        return jobRepo.findByJobStatus(JobStatus.DLQ);
    }

    public Job retryDLQJob(String jobId){
        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job Not Found"));

        if(!job.getJobStatus().equals(JobStatus.DLQ)){
            throw new RuntimeException("Job is not in DLQ");
        }
        job.setJobStatus(JobStatus.PENDING);
        job.setRetryCount(0);
        job.setErrorMessage(null);
        jobRepo.save(job);

        JobEvent jobEvent = new JobEvent(
                job.getId(),
                job.getJobType().toString(),
                job.getPayload(),
                job.getOwnerId(),
                0,
                ""               // ← not a notification event
        );

        kafkaProducer.publishJob(jobEvent);
        return job;
    }

    public StatsResponse getStats(){
        long pendingStatus = jobRepo.countByJobStatus(JobStatus.PENDING);
        long runningStatus = jobRepo.countByJobStatus(JobStatus.RUNNING);
        long failedStatus = jobRepo.countByJobStatus(JobStatus.FAILED);
        long completedStatus = jobRepo.countByJobStatus(JobStatus.COMPLETED);
        long dlqStatus = jobRepo.countByJobStatus(JobStatus.DLQ);
        long total = pendingStatus +
                runningStatus+
                failedStatus +
                completedStatus +
                dlqStatus;

        String successRate;
        long denominator = completedStatus + failedStatus + dlqStatus;
        if(denominator == 0) {
            successRate = "0%";
        } else {
            double rate = (double) completedStatus / denominator * 100;
            successRate = String.format("%.1f%%", rate);  // "80.0%"
        }
        StatsResponse statsResponse = new StatsResponse(
                total,
                pendingStatus,
                runningStatus,
                completedStatus,failedStatus,dlqStatus,
                successRate
        );
        return statsResponse;
    }

}
