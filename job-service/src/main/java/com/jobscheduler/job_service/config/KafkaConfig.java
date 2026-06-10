package com.jobscheduler.job_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic jobQueue() {
        return TopicBuilder
                .name("job-queue")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic jobDlq(){
        return TopicBuilder
                .name("job-dlq")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic jobEvents(){
        return TopicBuilder
                .name("job-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
