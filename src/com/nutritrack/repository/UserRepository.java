package com.nutritrack.repository;

import com.nutritrack.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository layer for User data management
 * Follows Repository Pattern for data access abstraction
 */
public class UserRepository {
    private static UserRepository instance;
    private List<User> users;
    
    private UserRepository() {
        this.users = new ArrayList<>();
    }
    
    /**
     * Singleton pattern for repository access
     */
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
    
    /**
     * Save a new user to the repository
     */
    public boolean save(User user) {
        if (findByUsername(user.getUsername()).isPresent()) {
            return false; // User already exists
        }
        users.add(user);
        return true;
    }
    
    /**
     * Find user by username
     */
    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
    
    /**
     * Get user by username and password
     */
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && 
                                   user.getPassword().equals(password))
                .findFirst();
    }
    
    /**
     * Get all users
     */
    public List<User> findAll() {
        return new ArrayList<>(users);
    }
    
    /**
     * Update existing user
     */
    public boolean update(User user) {
        Optional<User> existingUser = findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            User userToUpdate = existingUser.get();
            userToUpdate.setName(user.getName());
            userToUpdate.setHeight(user.getHeight());
            userToUpdate.setWeight(user.getWeight());
            userToUpdate.setAge(user.getAge());
            userToUpdate.setGender(user.getGender());
            userToUpdate.setActivityLevel(user.getActivityLevel());
            return true;
        }
        return false;
    }
    
    /**
     * Delete user by username
     */
    public boolean deleteByUsername(String username) {
        return users.removeIf(user -> user.getUsername().equals(username));
    }
    
    /**
     * Clear all users (for testing)
     */
    public void clearAll() {
        users.clear();
    }
}
