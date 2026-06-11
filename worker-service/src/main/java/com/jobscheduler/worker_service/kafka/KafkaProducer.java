package com.jobscheduler.worker_service.kafka;

import com.jobscheduler.worker_service.dto.JobEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<String, JobEvent> kafkaTemplate;

    public void publishJob(JobEvent jobEvent) {
        kafkaTemplate.send("job-queue", jobEvent);
    }

    public void publishToDLQ(JobEvent jobEvent) {
        kafkaTemplate.send("job-dlq", jobEvent);
    }

    public void publishJobEvent(JobEvent event) {
        kafkaTemplate.send("job-events", event);
    }
}
