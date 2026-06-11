package com.jobscheduler.admin_service.dto;

public record JobEvent(
        String jobId,
        String type,
        String payload,
        String ownerId,
        int retryCount,
        String status
) {
}
