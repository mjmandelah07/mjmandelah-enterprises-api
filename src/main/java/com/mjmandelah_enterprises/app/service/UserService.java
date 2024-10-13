package com.mjmandelah_enterprises.app.service;

import com.mjmandelah_enterprises.app.model.Role;
import com.mjmandelah_enterprises.app.model.User;
import com.mjmandelah_enterprises.app.repository.UserRepository;
import com.mjmandelah_enterprises.app.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // Registers a new user by encoding the password and ensuring uniqueness of email and username.
    public User registerUser(String firstName, String lastName, String username, String email, String password, Role role) {
        // Check if email is already in use
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        // Check if username is already in use
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists!");
        }

        // Default role assignment for users
        Role defaultRole = Role.USER;

        // Create new user entity and encode the password
        User newUser = new User(firstName, lastName, username, email, passwordEncoder.encode(password), defaultRole);

        // Save the new user to the database
        return userRepository.save(newUser);
    }



    // Verify the user email using code
    public Optional<User> verifyUser(String code) {
        Optional<User> userOptional = userRepository.findByVerificationCode(code);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the user is already verified
            if (user.isVerified()) {
                return Optional.empty(); // Return empty if the user is already verified
            }

            // Set user as verified
            user.setVerified(true);
            user.setVerificationCode(null); // Clear the verification code

            saveUser(user); // Save the updated user
            return Optional.of(user); // Return the updated user
        }

        return Optional.empty(); // Return empty if code is invalid
    }

    /**
     * Login service
     * @param username
     * @param password
     * @return jwt token
     */

    public Map<String, Object>  login(String username, String password) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );


            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

            // Check if the user is verified
            if (!user.isVerified()) {
                throw new RuntimeException("User is not verified. Please check your email to verify your account.");
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

            // Create a response map containing the token and user details
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", Map.of(
                    "id", user.getId(),
                    "username", user.getUsername(),
                    "email", user.getEmail(),
                    "role", user.getRole().name()
            ));

            return response;
        } catch (Exception e) {
            System.err.println("Login failed for user: " + username);
            e.printStackTrace();
            throw new RuntimeException("Invalid username or password!"); // Handle incorrect password
        }
    }

    /**
     *  Update user details
     *
     * @param user
     */

    public void saveUser(User user) {
        userRepository.save(user);   // This will save the user with the updated data's
    }

    /**
     * Find a user by their email.
     *
     * @return An Optional of User if found, or empty Optional if not found
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find a user by their username.
     *
     * @return An Optional of User if found, or empty Optional if not found
     */
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Check if a user exists by email.
     *
     * @return true if a user exists with the given email, false otherwise
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Check if a user exists by username.
     *
     * @return true if a user exists with the given username, false otherwise
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}