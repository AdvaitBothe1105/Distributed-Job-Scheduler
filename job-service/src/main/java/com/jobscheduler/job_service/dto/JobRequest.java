package com.jobscheduler.job_service.dto;

public record JobRequest(
        String type,
        String payload
) {
}
