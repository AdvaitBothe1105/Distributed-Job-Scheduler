package com.jobscheduler.auth_service.security;

import com.jobscheduler.auth_service.model.Customer;
import com.jobscheduler.auth_service.repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(customer.getEmail())
                .password(customer.getPassword())
                .roles(customer.getRole().name())
                .build();
    }
}
