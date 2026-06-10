package com.jobscheduler.worker_service.kafka;

import com.jobscheduler.worker_service.dto.JobEvent;
import com.jobscheduler.worker_service.service.WorkerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class JobConsumer {

    @Autowired
    private WorkerService workerService;

    @KafkaListener(
            topics = "job-queue",
            groupId = "worker-group",
            containerFactory = "manualAckListenerContainerFactory"
    )
    public void consume(
            ConsumerRecord<String, JobEvent> record,
            Acknowledgment acknowledgment
    ) {
        JobEvent jobEvent = record.value();
        System.out.println("Received Job: " + jobEvent.jobId() +
                " attempt: " + jobEvent.retryCount());

        workerService.processJob(jobEvent, acknowledgment);
    }
}
