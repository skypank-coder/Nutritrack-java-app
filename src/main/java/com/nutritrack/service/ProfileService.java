package com.nutritrack.service;

import com.nutritrack.model.User;
import com.nutritrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Simple Profile Service for NutriTrack
 * Not currently wired to any controller - kept for future use
 */
// @Service
public class ProfileService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get user profile data
     */
    public Map<String, Object> getProfile(Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            response.put("success", true);
            response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "name", user.getName(),
                "age", user.getAge(),
                "gender", user.getGender(),
                "height", user.getHeight(),
                "weight", user.getWeight(),
                "activityLevel", user.getActivityLevel()
            ));
            
            return response;
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching profile: " + e.getMessage());
            return response;
        }
    }
    
    /**
     * Update user profile data
     */
    public Map<String, Object> updateProfile(Long userId, Map<String, Object> profileData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return response;
            }
            
            User user = userOpt.get();
            
            // Update allowed fields
            if (profileData.containsKey("name")) {
                user.setName((String) profileData.get("name"));
            }
            if (profileData.containsKey("age")) {
                int age = ((Number) profileData.get("age")).intValue();
                if (age <= 0) {
                    response.put("success", false);
                    response.put("message", "Age must be positive");
                    return response;
                }
                user.setAge(age);
            }
            if (profileData.containsKey("gender")) {
                user.setGender((String) profileData.get("gender"));
            }
            if (profileData.containsKey("height")) {
                double height = ((Number) profileData.get("height")).doubleValue();
                if (height <= 0) {
                    response.put("success", false);
                    response.put("message", "Height must be positive");
                    return response;
                }
                user.setHeight(height);
            }
            if (profileData.containsKey("weight")) {
                double weight = ((Number) profileData.get("weight")).doubleValue();
                if (weight <= 0) {
                    response.put("success", false);
                    response.put("message", "Weight must be positive");
                    return response;
                }
                user.setWeight(weight);
            }
            if (profileData.containsKey("activityLevel")) {
                user.setActivityLevel((String) profileData.get("activityLevel"));
            }
            
            // Recalculate targets based on new profile
            user.setTargetCalories((int) user.getDailyCalorieRequirement());
            user.setTargetProtein(user.getWeight() * 1.6);
            user.setTargetCarbs((user.getTargetCalories() * 0.5) / 4);
            user.setTargetFat((user.getTargetCalories() * 0.25) / 9);
            
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "name", user.getName(),
                "age", user.getAge(),
                "gender", user.getGender(),
                "height", user.getHeight(),
                "weight", user.getWeight(),
                "activityLevel", user.getActivityLevel()
            ));
            
            return response;
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating profile: " + e.getMessage());
            return response;
        }
    }
}
