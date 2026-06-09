package com.jobscheduler.auth_service.controller;

import com.jobscheduler.auth_service.dto.LoginRequest;
import com.jobscheduler.auth_service.dto.LoginResponse;
import com.jobscheduler.auth_service.dto.RegisterRequest;
import com.jobscheduler.auth_service.model.Customer;
import com.jobscheduler.auth_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer register(@RequestBody RegisterRequest request){
        return authService.register(request);

    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // called by other services via Feign
    @GetMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String token) {
        String rawToken = token.replace("Bearer ","");
        return authService.validateToken(rawToken);
    }

    // called by other services via Feign
    @GetMapping("/cust/{email}")
    public Customer getCustByEmail(@PathVariable String email) {
        return authService.getUserByEmail(email);
    }
}
