package com.mjmandelah_enterprises.app.controller;

import com.mjmandelah_enterprises.app.model.Role;
import com.mjmandelah_enterprises.app.model.User;
import com.mjmandelah_enterprises.app.service.UserService;
import com.mjmandelah_enterprises.app.service.EmailService;
import com.mjmandelah_enterprises.app.util.VerificationCodeGenerator;
import com.mjmandelah_enterprises.app.dto.SignupRequest;
import com.mjmandelah_enterprises.app.dto.VerificationRequest;
import com.mjmandelah_enterprises.app.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    /**
     * Endpoint for user registration
     */
    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody SignupRequest signupRequest) {
        try {
            User newUser = userService.registerUser(
                    signupRequest.getFirstName(),
                    signupRequest.getLastName(),
                    signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    signupRequest.getPassword(),
                    Role.USER
            );

            // Generate verification code
            String verificationCode = VerificationCodeGenerator.generateCode();
            newUser.setVerificationCode(verificationCode);

            // Save the user again with the verification code
            userService.saveUser(newUser);

            // Send verification email
            String subject = "Verification Code for Mjmandelah Food";
            String body = "Your verification code is: " + verificationCode;
            emailService.sendSimpleEmail(newUser.getEmail(), subject, body);

            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Endpoint for verifying new user email
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestBody VerificationRequest verificationRequest) {
        Optional<User> verifiedUser = userService.verifyUser(verificationRequest.getCode());
        if (verifiedUser.isPresent()) {
            return ResponseEntity.ok("User verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification code or user already verified.");
        }
    }

    /**
     * Endpoint for user login
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Map<String, Object> response = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
