package com.nutritrack.controller;

import com.nutritrack.model.Meal;
import com.nutritrack.model.User;
import com.nutritrack.service.MealService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for meal tracking endpoints
 */
// Disabled - superseded by ApiController
// @RestController
public class MealController {
    
    @Autowired
    private MealService mealService;
    
    /**
     * Add a meal
     */
    @PostMapping("/add")
    public ResponseEntity<?> addMeal(@RequestBody Map<String, Object> mealData, HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Please login to add meals");
                return ResponseEntity.badRequest().body(response);
            }
            
            String mealType = (String) mealData.get("mealType");
            String foodKey = (String) mealData.get("foodKey");
            Double grams = (Double) mealData.get("grams");
            
            Meal meal = mealService.addMeal(user.getId(), mealType, foodKey, grams);
            meal.setUser(user); // Set the user for the meal
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Meal added successfully");
            response.put("meal", meal);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all meals for current user today
     */
    @GetMapping("/all")
    public ResponseEntity<?> getTodayMeals(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Please login to view meals");
            return ResponseEntity.badRequest().body(response);
        }
        
        List<Meal> meals = mealService.getMealsByUserAndDate(user, LocalDate.now());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("meals", meals);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get all available food names
     */
    @GetMapping("/foods")
    public ResponseEntity<?> getAllFoods() {
        List<String> foods = mealService.getAllFoodNames();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("foods", foods);
        
        return ResponseEntity.ok(response);
    }
}
