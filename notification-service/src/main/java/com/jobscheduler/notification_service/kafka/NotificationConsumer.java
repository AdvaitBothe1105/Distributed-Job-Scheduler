package com.jobscheduler.notification_service.kafka;

import com.jobscheduler.notification_service.dto.JobEvent;
import com.jobscheduler.notification_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationConsumer {
    
    @Autowired
    private NotificationService notificationService;

    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_DLQ = "DLQ";

    @KafkaListener(
            topics = "job-events",
            groupId = "notification-group"
    )

    public void consume(JobEvent jobEvent) {
        if(jobEvent.status().equals(STATUS_COMPLETED)) {
            notificationService.handleJobCompleted(jobEvent);
        } else if(jobEvent.status().equals(STATUS_DLQ)) {
            notificationService.handleJobFailed(jobEvent);
        } else {
            log.warn("Received job event with unknown status: {}",
                    jobEvent.status());
        }
    }
}
