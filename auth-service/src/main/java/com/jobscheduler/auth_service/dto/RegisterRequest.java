package com.jobscheduler.auth_service.dto;

public record RegisterRequest(
        String name,
        String email,
        String password
) {
}
