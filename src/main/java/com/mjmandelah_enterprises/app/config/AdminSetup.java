package com.mjmandelah_enterprises.app.config;

import com.mjmandelah_enterprises.app.model.Role;
import com.mjmandelah_enterprises.app.model.User;
import com.mjmandelah_enterprises.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSetup {

    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if the admin user already exists and create if not
            if (userRepository.findByUsername("admin").isEmpty()) {
                // Create a new admin user
                User adminUser = new User(
                        "Admin", "User", "admin", "admin@example.com",
                        passwordEncoder.encode("adminpassword"), Role.ADMIN
                );
                // Save the new admin user to the database
                userRepository.save(adminUser);
            }
        };
    }
}