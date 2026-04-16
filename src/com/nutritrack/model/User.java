package com.nutritrack.model;

import com.nutritrack.util.BMICalculator;
import com.nutritrack.util.BMRCalculator;

/**
 * User model class representing a user in the NutriTrack system
 * Follows proper encapsulation and uses utility classes for calculations
 */
public class User {
    private String username;
    private String password;
    private String name;
    private double height;
    private double weight;
    private int age;
    private String gender;
    private String activityLevel;
    
    public User(String username, String password, String name, double height, double weight, int age, String gender, String activityLevel) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.activityLevel = activityLevel;
    }
    
    // Getters with proper encapsulation
    public String getUsername() { 
        return username; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public String getName() { 
        return name; 
    }
    
    public double getHeight() { 
        return height; 
    }
    
    public double getWeight() { 
        return weight; 
    }
    
    public int getAge() { 
        return age; 
    }
    
    public String getGender() { 
        return gender; 
    }
    
    public String getActivityLevel() { 
        return activityLevel; 
    }
    
    // Setters with proper encapsulation
    public void setName(String name) { 
        this.name = name; 
    }
    
    public void setHeight(double height) { 
        this.height = height; 
    }
    
    public void setWeight(double weight) { 
        this.weight = weight; 
    }
    
    public void setAge(int age) { 
        this.age = age; 
    }
    
    public void setGender(String gender) { 
        this.gender = gender; 
    }
    
    public void setActivityLevel(String activityLevel) { 
        this.activityLevel = activityLevel; 
    }
    
    // Business methods using utility classes
    public double getBMI() {
        return BMICalculator.calculateBMI(weight, height);
    }
    
    public String getBMICategory() {
        return BMICalculator.getBMICategoryString(getBMI());
    }
    
    public double getBMR() {
        return BMRCalculator.calculateBMR(weight, height, age, gender);
    }
    
    public double getDailyCalorieRequirement() {
        return BMRCalculator.calculateDailyCalorieRequirement(weight, height, age, gender, activityLevel);
    }
    
    @Override
    public String toString() {
        return String.format("User{username='%s', name='%s', age=%d, height=%.2f, weight=%.1f, gender='%s'}",
                username, name, age, height, weight, gender);
    }
}
