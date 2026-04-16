package com.nutritrack.service;

import com.nutritrack.model.User;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.util.ValidationUtil;

import java.util.Optional;

/**
 * Authentication service layer
 * Handles all authentication-related business logic
 */
public class AuthService {
    private static AuthService instance;
    private UserRepository userRepository;
    private User currentUser;
    
    private AuthService() {
        this.userRepository = UserRepository.getInstance();
    }
    
    /**
     * Singleton pattern for service access
     */
    public static AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }
    
    /**
     * Register a new user
     */
    public AuthResult registerUser(String username, String password, String name, 
                                  double height, double weight, int age, 
                                  String gender, String activityLevel) {
        // Input validation
        if (!ValidationUtil.isValidUsername(username)) {
            return AuthResult.failure("Invalid username: " + ValidationUtil.getValidationErrorMessage("username", username));
        }
        
        if (!ValidationUtil.isValidPassword(password)) {
            return AuthResult.failure("Invalid password: " + ValidationUtil.getValidationErrorMessage("password", password));
        }
        
        if (!ValidationUtil.isValidName(name)) {
            return AuthResult.failure("Invalid name: " + ValidationUtil.getValidationErrorMessage("name", name));
        }
        
        if (!ValidationUtil.isValidHeight(height)) {
            return AuthResult.failure("Height must be between 0.5m and 3.0m");
        }
        
        if (!ValidationUtil.isValidWeight(weight)) {
            return AuthResult.failure("Weight must be between 20kg and 500kg");
        }
        
        if (!ValidationUtil.isValidAge(age)) {
            return AuthResult.failure("Age must be between 10 and 120");
        }
        
        // Create user object
        User user = new User(username, password, name, height, weight, age, gender, activityLevel);
        
        // Save to repository
        if (userRepository.save(user)) {
            return AuthResult.success("User registered successfully");
        } else {
            return AuthResult.failure("Username already exists");
        }
    }
    
    /**
     * Login user
     */
    public AuthResult loginUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return AuthResult.failure("Username cannot be empty");
        }
        
        if (password == null || password.isEmpty()) {
            return AuthResult.failure("Password cannot be empty");
        }
        
        Optional<User> userOpt = userRepository.findByUsernameAndPassword(username.trim(), password);
        if (userOpt.isPresent()) {
            this.currentUser = userOpt.get();
            return AuthResult.success("Login successful");
        } else {
            return AuthResult.failure("Invalid username or password");
        }
    }
    
    /**
     * Logout current user
     */
    public void logout() {
        this.currentUser = null;
    }
    
    /**
     * Get current logged-in user
     */
    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }
    
    /**
     * Update current user profile
     */
    public AuthResult updateCurrentUser(double height, double weight) {
        if (currentUser == null) {
            return AuthResult.failure("No user is currently logged in");
        }
        
        if (!ValidationUtil.isValidHeight(height)) {
            return AuthResult.failure("Height must be between 0.5m and 3.0m");
        }
        
        if (!ValidationUtil.isValidWeight(weight)) {
            return AuthResult.failure("Weight must be between 20kg and 500kg");
        }
        
        currentUser.setHeight(height);
        currentUser.setWeight(weight);
        
        if (userRepository.update(currentUser)) {
            return AuthResult.success("Profile updated successfully");
        } else {
            return AuthResult.failure("Failed to update profile");
        }
    }
    
    /**
     * Result wrapper for authentication operations
     */
    public static class AuthResult {
        private final boolean success;
        private final String message;
        
        private AuthResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public static AuthResult success(String message) {
            return new AuthResult(true, message);
        }
        
        public static AuthResult failure(String message) {
            return new AuthResult(false, message);
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
