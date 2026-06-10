package com.jobscheduler.worker_service.model;

public enum JobStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED,
    DLQ
}
