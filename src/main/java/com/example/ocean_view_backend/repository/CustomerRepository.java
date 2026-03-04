package com.example.ocean_view_backend.repository;

import com.example.ocean_view_backend.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    // Optional: find by email or phone to avoid duplicates
    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByPhone(String phone);
}