package com.jobscheduler.admin_service.model;

import com.jobscheduler.admin_service.model.JobType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    private String payload;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus jobStatus;

    @Column(nullable = false)
    private int retryCount = 0;

    @Column(nullable = false)
    private String ownerId;

    private String errorMessage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    // add this so createdAt is set automatically
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.jobStatus = JobStatus.PENDING;
        this.retryCount = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
