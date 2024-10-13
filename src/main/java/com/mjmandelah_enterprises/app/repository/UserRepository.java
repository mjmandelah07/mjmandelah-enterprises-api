package com.mjmandelah_enterprises.app.repository;

import com.mjmandelah_enterprises.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by username
    Optional<User> findByUsername(String username);

    // Check if a user exists by email
    Boolean existsByEmail(String email);

    // Check if a user exists by username
    Boolean existsByUsername(String username);

    // Find user by verification code
    Optional<User> findByVerificationCode(String verificationCode);
}