package com.jobscheduler.auth_service.dto;

public record LoginResponse(
        String token,
        String email,
        String name,
        String role
) {
}
