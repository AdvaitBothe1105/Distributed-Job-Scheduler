package com.jobscheduler.job_service.kafka;

import com.jobscheduler.job_service.dto.JobEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, JobEvent> kafkaTemplate;

    private static final String TOPIC = "job-queue";

    public void publishJob(JobEvent jobEvent) {
        kafkaTemplate.send(TOPIC, jobEvent);
    }
}
