package com.nutritrack.controller;

import com.nutritrack.model.User;
import com.nutritrack.service.MealService;
import com.nutritrack.service.UserService;
import com.nutritrack.util.BMICalculator;
import com.nutritrack.util.BMRCalculator;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for nutrition and health endpoints
 */
// This controller is superseded by ApiController - disabled to avoid route conflicts
// @RestController
// @RequestMapping("/api/nutrition")
// @CrossOrigin(origins = "*")
public class NutritionController {
    
    @Autowired
    private MealService mealService;
    
    @Autowired
    private UserService userService;
    
    /**
     * Get nutrition summary for today
     */
    @GetMapping("/summary")
    public ResponseEntity<?> getNutritionSummary(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Please login to view nutrition summary");
            return ResponseEntity.badRequest().body(response);
        }
        
        Map<String, Double> dailyNutrition = mealService.getDailyNutritionSummary(user, LocalDate.now());
        Map<String, Double> weeklyNutrition = mealService.getWeeklyNutritionSummary(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("daily", dailyNutrition);
        response.put("weekly", weeklyNutrition);
        response.put("targetCalories", user.getDailyCalorieRequirement());
        response.put("remainingCalories", user.getDailyCalorieRequirement() - dailyNutrition.get("calories"));
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Calculate BMI
     */
    @PostMapping("/bmi")
    public ResponseEntity<?> calculateBMI(@RequestBody Map<String, Double> bmiData) {
        try {
            double weight = bmiData.get("weight");
            double height = bmiData.get("height");
            
            double bmi = BMICalculator.calculateBMI(weight, height);
            String category = BMICalculator.getBMICategoryString(bmi);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("bmi", bmi);
            response.put("category", category);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "BMI calculation failed: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Calculate BMR
     */
    @PostMapping("/bmr")
    public ResponseEntity<?> calculateBMR(@RequestBody Map<String, Object> bmrData) {
        try {
            double weight = (Double) bmrData.get("weight");
            double height = (Double) bmrData.get("height");
            int age = (Integer) bmrData.get("age");
            String gender = (String) bmrData.get("gender");
            String activityLevel = (String) bmrData.get("activityLevel");
            
            double bmr = BMRCalculator.calculateBMR(weight, height, age, gender);
            double dailyTarget = BMRCalculator.calculateDailyCalorieRequirement(bmr, activityLevel);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("bmr", bmr);
            response.put("dailyTarget", dailyTarget);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "BMR calculation failed: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Update user profile
     */
    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody Map<String, Double> profileData, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Please login to update profile");
                return ResponseEntity.badRequest().body(response);
            }
            
            double height = profileData.get("height");
            double weight = profileData.get("weight");
            
            User updatedUser = userService.updateUserProfile(user.getId(), height, weight);
            
            // Update session user
            session.setAttribute("user", updatedUser);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("user", updatedUser);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
}
