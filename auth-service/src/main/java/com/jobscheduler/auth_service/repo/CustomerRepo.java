package com.jobscheduler.auth_service.repo;

import com.jobscheduler.auth_service.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
}
