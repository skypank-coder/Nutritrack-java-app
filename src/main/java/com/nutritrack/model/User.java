package com.nutritrack.model;

import com.nutritrack.util.BMICalculator;
import com.nutritrack.util.BMRCalculator;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * User entity for NutriTrack web application
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    @NotBlank
    private String username;
    
    @Column(nullable = false)
    @NotBlank
    private String password;
    
    @Column(nullable = false)
    @NotBlank
    private String name;
    
    @Column(nullable = false)
    private double height;
    
    @Column(nullable = false)
    private double weight;
    
    @Column(nullable = false)
    private int age;
    
    @Column(nullable = false)
    @NotBlank
    private String gender;
    
    @Column(nullable = false)
    @NotBlank
    private String activityLevel;
    
    @Column(nullable = false)
    private int targetCalories;
    
    @Column(nullable = false)
    private double targetProtein;
    
    @Column(nullable = false)
    private double targetCarbs;
    
    @Column(nullable = false)
    private double targetFat;
    
    @Column(nullable = true)
    private String goalType; // lose_weight, maintain, gain_weight
    
    @Column(nullable = true)
    private double goalWeight;
    
    @Column(nullable = true)
    private int goalWeeks;
    
    @Column(nullable = true)
    private String motivationalQuote;
    
    @Column(nullable = true)
    private LocalDate lastQuoteDate;
    
    // Default constructor for JPA
    public User() {}
    
    public User(String username, String password, String name, double height, double weight, int age, String gender, String activityLevel) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.activityLevel = activityLevel;
        // Calculate default targets
        this.targetCalories = (int) getDailyCalorieRequirement();
        this.targetProtein = weight * 1.6; // 1.6g per kg
        this.targetCarbs = (targetCalories * 0.5) / 4; // 50% from carbs
        this.targetFat = (targetCalories * 0.25) / 9; // 25% from fat
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }
    
    public int getTargetCalories() { return targetCalories; }
    public void setTargetCalories(int targetCalories) { this.targetCalories = targetCalories; }
    
    public double getTargetProtein() { return targetProtein; }
    public void setTargetProtein(double targetProtein) { this.targetProtein = targetProtein; }
    
    public double getTargetCarbs() { return targetCarbs; }
    public void setTargetCarbs(double targetCarbs) { this.targetCarbs = targetCarbs; }
    
    public double getTargetFat() { return targetFat; }
    public void setTargetFat(double targetFat) { this.targetFat = targetFat; }
    
    public String getGoalType() { return goalType; }
    public void setGoalType(String goalType) { this.goalType = goalType; }
    
    public double getGoalWeight() { return goalWeight; }
    public void setGoalWeight(double goalWeight) { this.goalWeight = goalWeight; }
    
    public int getGoalWeeks() { return goalWeeks; }
    public void setGoalWeeks(int goalWeeks) { this.goalWeeks = goalWeeks; }
    
    public String getMotivationalQuote() { return motivationalQuote; }
    public void setMotivationalQuote(String motivationalQuote) { this.motivationalQuote = motivationalQuote; }
    
    public LocalDate getLastQuoteDate() { return lastQuoteDate; }
    public void setLastQuoteDate(LocalDate lastQuoteDate) { this.lastQuoteDate = lastQuoteDate; }
    
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
        return String.format("User{id=%d, username='%s', name='%s', age=%d, height=%.2f, weight=%.1f, gender='%s'}",
                id, username, name, age, height, weight, gender);
    }
}
