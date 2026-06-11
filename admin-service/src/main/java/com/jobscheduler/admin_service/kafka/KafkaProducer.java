package com.jobscheduler.admin_service.kafka;

import com.jobscheduler.admin_service.dto.JobEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, JobEvent> kafkaTemplate;

    private static final String JOB_QUEUE = "job-queue";

    public void publishJob(JobEvent event) {
       kafkaTemplate.send(JOB_QUEUE, event);
       log.info("Republished job {} from DLQ to job-queue", event.jobId());
    }
}
