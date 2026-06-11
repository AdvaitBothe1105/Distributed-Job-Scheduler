package com.jobscheduler.notification_service.service;

import com.jobscheduler.notification_service.dto.JobEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void handleJobCompleted(JobEvent event) {
        log.info("=============================");
        log.info("JOB COMPLETED NOTIFICATION");
        log.info("Job ID:    {}", event.jobId());
        log.info("Type:      {}", event.type());
        log.info("Owner:     {}", event.ownerId());
        log.info("Status:    {}", event.status());
        log.info("=============================");
        // in real app → send email, push notification etc
    }

    public void handleJobFailed(JobEvent event) {
        log.warn("=============================");
        log.warn("JOB FAILED ALERT");
        log.warn("Job ID:    {}", event.jobId());
        log.warn("Type:      {}", event.type());
        log.warn("Owner:     {}", event.ownerId());
        log.warn("Retries:   {}", event.retryCount());
        log.warn("=============================");
        // in real app → send alert email, Slack notification etc
    }
}
