package com.nutritrack.service;

import com.nutritrack.model.User;
import com.nutritrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Simple Goal Service for NutriTrack
 * Not currently wired to any controller - kept for future use
 */
// @Service
public class SimpleGoalService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Get user goal data
     */
    public Map<String, Object> getGoals(Long userId) {
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
            response.put("goals", Map.of(
                "targetCalories", user.getTargetCalories(),
                "targetProtein", user.getTargetProtein(),
                "targetCarbs", user.getTargetCarbs(),
                "targetFat", user.getTargetFat(),
                "goalType", user.getGoalType(),
                "targetWeight", user.getGoalWeight(),
                "durationWeeks", user.getGoalWeeks()
            ));
            
            return response;
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching goals: " + e.getMessage());
            return response;
        }
    }
    
    /**
     * Update user goal data
     */
    public Map<String, Object> updateGoals(Long userId, Map<String, Object> goalData) {
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
            if (goalData.containsKey("targetCalories")) {
                int calories = ((Number) goalData.get("targetCalories")).intValue();
                if (calories <= 0) {
                    response.put("success", false);
                    response.put("message", "Target calories must be positive");
                    return response;
                }
                user.setTargetCalories(calories);
            }
            if (goalData.containsKey("targetProtein")) {
                double protein = ((Number) goalData.get("targetProtein")).doubleValue();
                if (protein <= 0) {
                    response.put("success", false);
                    response.put("message", "Target protein must be positive");
                    return response;
                }
                user.setTargetProtein(protein);
            }
            if (goalData.containsKey("targetCarbs")) {
                double carbs = ((Number) goalData.get("targetCarbs")).doubleValue();
                if (carbs <= 0) {
                    response.put("success", false);
                    response.put("message", "Target carbs must be positive");
                    return response;
                }
                user.setTargetCarbs(carbs);
            }
            if (goalData.containsKey("targetFat")) {
                double fat = ((Number) goalData.get("targetFat")).doubleValue();
                if (fat <= 0) {
                    response.put("success", false);
                    response.put("message", "Target fat must be positive");
                    return response;
                }
                user.setTargetFat(fat);
            }
            if (goalData.containsKey("goalType")) {
                user.setGoalType((String) goalData.get("goalType"));
            }
            if (goalData.containsKey("targetWeight")) {
                double targetWeight = ((Number) goalData.get("targetWeight")).doubleValue();
                if (targetWeight <= 0) {
                    response.put("success", false);
                    response.put("message", "Target weight must be positive");
                    return response;
                }
                user.setGoalWeight(targetWeight);
            }
            if (goalData.containsKey("durationWeeks")) {
                int weeks = ((Number) goalData.get("durationWeeks")).intValue();
                if (weeks <= 0) {
                    response.put("success", false);
                    response.put("message", "Duration weeks must be positive");
                    return response;
                }
                user.setGoalWeeks(weeks);
            }
            
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Goals updated successfully");
            response.put("goals", Map.of(
                "targetCalories", user.getTargetCalories(),
                "targetProtein", user.getTargetProtein(),
                "targetCarbs", user.getTargetCarbs(),
                "targetFat", user.getTargetFat(),
                "goalType", user.getGoalType(),
                "targetWeight", user.getGoalWeight(),
                "durationWeeks", user.getGoalWeeks()
            ));
            
            return response;
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating goals: " + e.getMessage());
            return response;
        }
    }
    
    /**
     * Calculate simple goal recommendations
     */
    public Map<String, Object> calculateRecommendations(User user) {
        Map<String, Object> recommendations = new HashMap<>();
        
        if (user.getGoalType() == null) {
            recommendations.put("message", "Set a goal type to get recommendations");
            return recommendations;
        }
        
        if ("LOSE_WEIGHT".equals(user.getGoalType())) {
            recommendations.put("message", "For weight loss, consider a calorie deficit of 500 calories per day");
            recommendations.put("suggestedCalories", user.getTargetCalories() - 500);
        } else if ("GAIN_WEIGHT".equals(user.getGoalType())) {
            recommendations.put("message", "For weight gain, consider a calorie surplus of 500 calories per day");
            recommendations.put("suggestedCalories", user.getTargetCalories() + 500);
        } else {
            recommendations.put("message", "For maintenance, keep your current calorie target");
            recommendations.put("suggestedCalories", user.getTargetCalories());
        }
        
        return recommendations;
    }
}
