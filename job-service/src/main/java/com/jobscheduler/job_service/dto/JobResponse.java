package com.jobscheduler.job_service.dto;

import java.time.LocalDateTime;

public record JobResponse(
        String id,
        String type,
        String status,
        String ownerId,
        LocalDateTime createdAt
) {
}
