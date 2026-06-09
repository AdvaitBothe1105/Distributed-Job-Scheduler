package com.jobscheduler.auth_service.dto;

public record LoginRequest(
        String email,
        String password
) {
}
