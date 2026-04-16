package com.nutritrack.service;

import com.nutritrack.model.User;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User service for NutriTrack web application
 * Handles user registration, login, and management
 */
// Disabled - uses model.User which conflicts with entity.User in UserRepository
// @Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * Register a new user
     */
    public User registerUser(String username, String password, String name, 
                           double height, double weight, int age, 
                           String gender, String activityLevel) {
        
        // Validation
        if (!ValidationUtil.isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username: " + ValidationUtil.getValidationErrorMessage("username", username));
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password: " + ValidationUtil.getValidationErrorMessage("password", password));
        }
        
        if (!ValidationUtil.isValidName(name)) {
            throw new IllegalArgumentException("Invalid name: " + ValidationUtil.getValidationErrorMessage("name", name));
        }
        
        if (!ValidationUtil.isValidHeight(height)) {
            throw new IllegalArgumentException("Height must be between 0.5m and 3.0m");
        }
        
        if (!ValidationUtil.isValidWeight(weight)) {
            throw new IllegalArgumentException("Weight must be between 20kg and 500kg");
        }
        
        if (!ValidationUtil.isValidAge(age)) {
            throw new IllegalArgumentException("Age must be between 10 and 120");
        }
        
        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        
        // Create and save user
        User user = new User(username, passwordEncoder.encode(password), name, height, weight, age, gender, activityLevel);
        return userRepository.save(user);
    }
    
    /**
     * Authenticate user login
     */
    public Optional<User> authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Get user by username
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    /**
     * Update user profile
     */
    public User updateUserProfile(Long userId, double height, double weight) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User user = userOpt.get();
        
        if (!ValidationUtil.isValidHeight(height)) {
            throw new IllegalArgumentException("Height must be between 0.5m and 3.0m");
        }
        
        if (!ValidationUtil.isValidWeight(weight)) {
            throw new IllegalArgumentException("Weight must be between 20kg and 500kg");
        }
        
        user.setHeight(height);
        user.setWeight(weight);
        
        return userRepository.save(user);
    }
}
