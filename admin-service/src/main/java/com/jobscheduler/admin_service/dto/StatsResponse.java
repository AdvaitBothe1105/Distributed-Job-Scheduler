package com.jobscheduler.admin_service.dto;

public record StatsResponse(
        long total,
        long pending,
        long running,
        long completed,
        long failed,
        long inDLQ,
        String successRate
) {
}
