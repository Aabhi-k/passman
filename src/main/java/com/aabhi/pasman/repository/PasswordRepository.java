package com.aabhi.pasman.repository;

import com.aabhi.pasman.model.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password, String> {
    // Custom query methods can be defined here if needed
    // For example, find by userId or any other criteria
}
