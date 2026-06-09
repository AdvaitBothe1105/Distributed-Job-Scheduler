package com.jobscheduler.auth_service.service;

import com.jobscheduler.auth_service.dto.LoginRequest;
import com.jobscheduler.auth_service.dto.LoginResponse;
import com.jobscheduler.auth_service.dto.RegisterRequest;
import com.jobscheduler.auth_service.exception.DuplicateResourceException;
import com.jobscheduler.auth_service.exception.ResourceNotFound;
import com.jobscheduler.auth_service.model.Customer;
import com.jobscheduler.auth_service.model.Role;
import com.jobscheduler.auth_service.repo.CustomerRepo;
import com.jobscheduler.auth_service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public Customer register(RegisterRequest request) {
        if(customerRepo.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email is already in use");
        }
        Customer customer = new Customer();
        customer.setEmail(request.email());
        customer.setName(request.name());
        customer.setPassword(bCryptPasswordEncoder.encode(request.password()));
        customer.setRole(Role.USER);
        return customerRepo.save(customer);
    }

    public LoginResponse login (LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        Customer customer = customerRepo.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFound("User Not found"));

        String token = jwtUtil.generateToken(customer.getEmail());
        return new LoginResponse(
                token,
                customer.getEmail(),
                customer.getName(),
                customer.getRole().name()
        );
    }

    // used by other services via Feign to validate token
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    // used by other services via Feign to get user details
    public Customer getUserByEmail(String email) {
        return customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User Not Found"));
    }

}
