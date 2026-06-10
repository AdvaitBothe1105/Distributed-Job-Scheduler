package com.jobscheduler.worker_service.service;

import com.jobscheduler.worker_service.dto.JobEvent;
import com.jobscheduler.worker_service.kafka.KafkaProducer;
import com.jobscheduler.worker_service.model.Job;
import com.jobscheduler.worker_service.model.JobStatus;
import com.jobscheduler.worker_service.repo.JobRepo;
import org.springframework.kafka.support.Acknowledgment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class WorkerService {

    @Autowired
    private JobRepo jobRepo;

    @Autowired
    private JobExecutorService jobExecutorService;

    @Autowired
    private KafkaProducer kafkaProducer;

    private static final int MAX_RETRIES = 3;

    public void processJob(JobEvent event, Acknowledgment ack) {

        Job job = jobRepo.findById(event.jobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // idempotency check
        if(job.getJobStatus() == JobStatus.COMPLETED) {
            log.info("Job {} already processed — skipping", job.getId());
            ack.acknowledge();
            return;
        }

        // mark as running
        job.setJobStatus(JobStatus.RUNNING);
        jobRepo.save(job);

        try {
            // execute job
            jobExecutorService.executeEvent(event);

            // success — update status
            job.setJobStatus(JobStatus.COMPLETED);
            job.setCompletedAt(LocalDateTime.now());
            jobRepo.save(job);

            // ONLY acknowledge after full success
            ack.acknowledge();
            log.info("Job {} completed successfully", job.getId());

        } catch (Exception e) {
            log.error("Job {} failed: {}", job.getId(), e.getMessage());
            handleFailure(event, e);
            // NO ack here — intentional
        }
    }

    public void handleFailure(JobEvent event, Exception e) {
        Job job = jobRepo.findById(event.jobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if(event.retryCount() < MAX_RETRIES) {

            // exponential backoff
            long waitTime = (long) Math.pow(2, event.retryCount()) * 1000;
            log.info("Waiting {}ms before retry attempt {}",
                    waitTime, event.retryCount() + 1);

            try {
                Thread.sleep(waitTime);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            // create retry event with incremented count
            JobEvent retryEvent = new JobEvent(
                    event.jobId(),
                    event.type(),
                    event.payload(),
                    event.ownerId(),
                    event.retryCount() + 1
            );

            // update DB before republishing
            job.setRetryCount(event.retryCount() + 1);
            job.setJobStatus(JobStatus.FAILED);
            jobRepo.save(job);

            // republish for retry
            kafkaProducer.publishJob(retryEvent);
            log.info("Job {} queued for retry attempt {}",
                    event.jobId(), event.retryCount() + 1);

        } else {
            // max retries exceeded — send to DLQ
            kafkaProducer.publishToDLQ(event);

            job.setJobStatus(JobStatus.DLQ);
            job.setErrorMessage(e.getMessage());
            jobRepo.save(job);

            log.info("Job {} sent to DLQ after {} retries",
                    event.jobId(), MAX_RETRIES);
        }
    }
}
