package com.nutritrack.controller;

import com.nutritrack.model.Meal;
import com.nutritrack.model.User;
import com.nutritrack.repository.MealRepository;
import com.nutritrack.repository.UserRepository;
import com.nutritrack.service.NutritionService;
import com.nutritrack.service.MotivationalQuotesService;
import com.nutritrack.util.CalculationUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Main controller for NutriTrack application
 */
@Controller
public class NutriTrackController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MealRepository mealRepository;
    
    @Autowired
    private NutritionService nutritionService;
    
    @Autowired
    private MotivationalQuotesService motivationalQuotesService;
    
        
    /**
     * Serve login page
     */
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }
    
    /**
     * Serve dashboard page (clean URL)
     */
    @GetMapping("/dashboard")
    public String dashboardClean(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/";
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) { session.invalidate(); return "redirect:/"; }
        model.addAttribute("user", userOpt.get());
        return "dashboard-working";
    }

    /**
     * Serve dashboard page
     */
    @GetMapping("/dashboard-working.html")
    public String dashboardPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/";
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/";
        }
        
        User user = userOpt.get();
        model.addAttribute("user", user);
        return "dashboard-working";
    }
    
    /**
     * Serve add meal page
     */
    @GetMapping("/add-meal.html")
    public String addMealPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/";
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/";
        }
        
        User user = userOpt.get();
        model.addAttribute("user", user);
        return "add-meal";
    }
    
    /**
     * Serve goals page
     */
    @GetMapping("/goals")
    public String goalsPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/";
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) { session.invalidate(); return "redirect:/"; }
        model.addAttribute("user", userOpt.get());
        return "goals";
    }

    /**
     * Serve profile page
     */
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/";
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/";
        }
        
        User user = userOpt.get();
        model.addAttribute("user", user);
        return "profile";
    }
    
        
    /**
     * Serve reports page
     */
    @GetMapping("/reports.html")
    public String reportsPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/";
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/";
        }
        
        User user = userOpt.get();
        model.addAttribute("user", user);
        return "reports";
    }
    
    /**
     * Serve BMI calculator page (clean URL)
     */
    @GetMapping("/bmi")
    public String bmiPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return "redirect:/";
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) { session.invalidate(); return "redirect:/"; }
        model.addAttribute("user", userOpt.get());
        return "bmi-calculator";
    }

    /**
     * Serve BMI calculator page
     */
    @GetMapping("/bmi-calculator.html")
    public String bmiCalculatorPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/";
        }
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/";
        }
        
        User user = userOpt.get();
        model.addAttribute("user", user);
        return "bmi-calculator";
    }
    
    /**
     * Handle user registration
     */
    @PostMapping("/api/register")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> register(@RequestParam String username,
                                                        @RequestParam String password,
                                                        @RequestParam String name,
                                                        @RequestParam double height,
                                                        @RequestParam double weight,
                                                        @RequestParam int age,
                                                        @RequestParam String gender,
                                                        @RequestParam String activityLevel) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if user already exists
            if (userRepository.existsByUsername(username)) {
                response.put("success", false);
                response.put("message", "Username already exists");
                return ResponseEntity.ok(response);
            }
            
            // Create new user
            User newUser = new User(username, password, name, height, weight, age, gender, activityLevel);
            userRepository.save(newUser);
            
            System.out.println("New user registered: " + username);
            
            response.put("success", true);
            response.put("message", "Registration successful");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Handle user login
     */
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(@RequestParam String username,
                                                    @RequestParam String password,
                                                    HttpSession session) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOpt = userRepository.findByUsername(username);
            
            if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
                User user = userOpt.get();
                
                // Create session
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                
                System.out.println("Login SUCCESS for user: " + username);
                
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", Map.of(
                    "name", user.getName(),
                    "username", user.getUsername(),
                    "id", user.getId()
                ));
                
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Login FAILED for user: " + username);
                response.put("success", false);
                response.put("message", "Invalid username or password");
                return ResponseEntity.ok(response);
            }
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Login failed: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Get nutrition summary for logged-in user
     */
    @GetMapping("/api/nutrition/summary")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getNutritionSummary(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.ok(response);
            }
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            LocalDate today = LocalDate.now();
            
            // Get today's meals
            List<Meal> todayMeals = mealRepository.findByUserAndDate(user, today);
            
            // Calculate totals with proper rounding
            double totalCalories = CalculationUtils.roundToTwoDecimals(todayMeals.stream().mapToDouble(Meal::getCalories).sum());
            double totalProtein = CalculationUtils.roundToTwoDecimals(todayMeals.stream().mapToDouble(Meal::getProtein).sum());
            double totalCarbs = CalculationUtils.roundToTwoDecimals(todayMeals.stream().mapToDouble(Meal::getCarbs).sum());
            double totalFat = CalculationUtils.roundToTwoDecimals(todayMeals.stream().mapToDouble(Meal::getFat).sum());
            
            // Calculate weekly averages with proper rounding
            LocalDate weekStart = today.minusDays(6);
            List<Meal> weeklyMeals = mealRepository.findByUserAndDateBetween(user, weekStart, today);
            
            double weeklyTotalCalories = weeklyMeals.stream().mapToDouble(Meal::getCalories).sum();
            double weeklyTotalProtein = weeklyMeals.stream().mapToDouble(Meal::getProtein).sum();
            double weeklyTotalCarbs = weeklyMeals.stream().mapToDouble(Meal::getCarbs).sum();
            double weeklyTotalFat = weeklyMeals.stream().mapToDouble(Meal::getFat).sum();
            
            // Weekly avg: always divide by 7 (total last 7 days / 7)
            double weeklyAvgCalories = CalculationUtils.calculateAverageCalories(weeklyTotalCalories, 7);
            double weeklyAvgProtein = CalculationUtils.calculateAverageCalories(weeklyTotalProtein, 7);
            double weeklyAvgCarbs = CalculationUtils.calculateAverageCalories(weeklyTotalCarbs, 7);
            double weeklyAvgFat = CalculationUtils.calculateAverageCalories(weeklyTotalFat, 7);
            
            int remainingCalories = CalculationUtils.calculateRemainingCalories(user.getTargetCalories(), totalCalories);
            double progress = user.getTargetCalories() > 0
                ? CalculationUtils.roundToTwoDecimals((totalCalories / user.getTargetCalories()) * 100)
                : 0;
            
            response.put("success", true);
            response.put("daily", Map.of(
                "calories", (int) totalCalories,
                "protein", totalProtein,
                "carbs", totalCarbs,
                "fat", totalFat
            ));
            response.put("weekly", Map.of(
                "calories", (int) weeklyAvgCalories,
                "protein", weeklyAvgProtein,
                "carbs", weeklyAvgCarbs,
                "fat", weeklyAvgFat
            ));
            response.put("targetCalories", user.getTargetCalories());
            response.put("remainingCalories", remainingCalories);
            response.put("progress", progress);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Nutrition summary error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Error fetching nutrition data: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Get today's meals for logged-in user
     */
    @GetMapping("/api/meal/all")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTodayMeals(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.ok(response);
            }
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            List<Meal> todayMeals = mealRepository.findByUserAndDate(user, LocalDate.now());
            
            List<Map<String, Object>> mealsData = new ArrayList<>();
            for (Meal meal : todayMeals) {
                Map<String, Object> mealData = new HashMap<>();
                mealData.put("mealType", meal.getMealType());
                mealData.put("foodName", meal.getFoodName());
                mealData.put("grams", meal.getGrams());
                mealData.put("calories", meal.getCalories());
                mealData.put("protein", meal.getProtein());
                mealData.put("carbs", meal.getCarbs());
                mealData.put("fat", meal.getFat());
                mealsData.add(mealData);
            }
            
            response.put("success", true);
            response.put("meals", mealsData);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Get meals error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Error fetching meals: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Add meal for logged-in user
     */
    @PostMapping("/api/meal/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addMeal(@RequestBody Map<String, Object> mealData,
                                                     HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.ok(response);
            }
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            
            String mealType = (String) mealData.get("mealType");
            String foodName = (String) mealData.get("foodName");
            double grams = ((Number) mealData.get("grams")).doubleValue();
            
            // Calculate nutrition based on food
            Map<String, Double> nutrition = nutritionService.calculateNutrition(foodName, grams);
            
            // Create and save meal
            Meal newMeal = new Meal();
            newMeal.setUser(user);
            newMeal.setMealType(mealType);
            newMeal.setFoodName(foodName);
            newMeal.setGrams(grams);
            newMeal.setDate(LocalDate.now());
            newMeal.setCalories(nutrition.get("calories"));
            newMeal.setProtein(nutrition.get("protein"));
            newMeal.setCarbs(nutrition.get("carbs"));
            newMeal.setFat(nutrition.get("fat"));
            
            mealRepository.save(newMeal);
            
            System.out.println("Meal added: " + foodName + " (" + grams + "g) for user " + user.getUsername());
            
            response.put("success", true);
            response.put("message", "Meal added successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Add meal error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Error adding meal: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Get available food items
     */
    @GetMapping("/api/meal/foods")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getFoodItems() {
        Map<String, Object> response = new HashMap<>();
        
        List<String> foods = Arrays.asList(
            "chicken", "rice", "bread", "egg", "milk", "apple", "banana", "spinach", "broccoli",
            "dal", "biryani", "samosa", "dosa", "yogurt", "paneer", "tofu", "fish", "almonds",
            "olive_oil", "avocado", "orange", "carrot", "tomato", "oats", "roti", "dal_tadka"
        );
        
        response.put("success", true);
        response.put("foods", foods);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get user goals
     */
    @GetMapping("/api/goals")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserGoals(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) { response.put("success", false); response.put("message", "User not logged in"); return ResponseEntity.ok(response); }
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) { response.put("success", false); response.put("message", "User not found"); return ResponseEntity.ok(response); }
            User user = userOpt.get();
            Map<String, Object> goals = new HashMap<>();
            goals.put("targetCalories", user.getTargetCalories());
            goals.put("targetProtein", CalculationUtils.roundToTwoDecimals(user.getTargetProtein()));
            goals.put("targetCarbs", CalculationUtils.roundToTwoDecimals(user.getTargetCarbs()));
            goals.put("targetFat", CalculationUtils.roundToTwoDecimals(user.getTargetFat()));
            goals.put("goalType", user.getGoalType());
            goals.put("goalWeight", user.getGoalWeight());
            goals.put("goalWeeks", user.getGoalWeeks());
            response.put("success", true);
            response.put("goals", goals);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false); response.put("message", "Error fetching goals: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Update user goals
     */
    @PutMapping("/api/goals")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateUserGoals(@RequestBody Map<String, Object> goalData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) { response.put("success", false); response.put("message", "User not logged in"); return ResponseEntity.ok(response); }
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) { response.put("success", false); response.put("message", "User not found"); return ResponseEntity.ok(response); }
            User user = userOpt.get();
            if (goalData.containsKey("targetCalories")) user.setTargetCalories(((Number) goalData.get("targetCalories")).intValue());
            if (goalData.containsKey("targetProtein")) user.setTargetProtein(((Number) goalData.get("targetProtein")).doubleValue());
            if (goalData.containsKey("targetCarbs")) user.setTargetCarbs(((Number) goalData.get("targetCarbs")).doubleValue());
            if (goalData.containsKey("targetFat")) user.setTargetFat(((Number) goalData.get("targetFat")).doubleValue());
            if (goalData.containsKey("goalType")) user.setGoalType((String) goalData.get("goalType"));
            if (goalData.containsKey("goalWeight")) user.setGoalWeight(((Number) goalData.get("goalWeight")).doubleValue());
            if (goalData.containsKey("durationWeeks")) user.setGoalWeeks(((Number) goalData.get("durationWeeks")).intValue());
            if (goalData.containsKey("goalWeeks")) user.setGoalWeeks(((Number) goalData.get("goalWeeks")).intValue());
            userRepository.save(user);
            response.put("success", true);
            response.put("message", "Goals updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false); response.put("message", "Error updating goals: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Get user profile
     */
    @GetMapping("/api/profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getProfile(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) { response.put("success", false); response.put("message", "User not logged in"); return ResponseEntity.ok(response); }
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) { response.put("success", false); response.put("message", "User not found"); return ResponseEntity.ok(response); }
            User user = userOpt.get();
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("name", user.getName());
            userMap.put("weight", user.getWeight());
            userMap.put("height", user.getHeight());
            userMap.put("age", user.getAge());
            userMap.put("gender", user.getGender());
            userMap.put("activityLevel", user.getActivityLevel());
            userMap.put("targetCalories", user.getTargetCalories());
            userMap.put("targetProtein", CalculationUtils.roundToTwoDecimals(user.getTargetProtein()));
            userMap.put("targetCarbs", CalculationUtils.roundToTwoDecimals(user.getTargetCarbs()));
            userMap.put("targetFat", CalculationUtils.roundToTwoDecimals(user.getTargetFat()));
            userMap.put("goalType", user.getGoalType());
            userMap.put("goalWeight", user.getGoalWeight());
            userMap.put("goalWeeks", user.getGoalWeeks());
            response.put("success", true);
            response.put("user", userMap);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false); response.put("message", "Error fetching profile: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * Update user profile
     */
    @PutMapping("/api/profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, Object> profileData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) { response.put("success", false); response.put("message", "User not logged in"); return ResponseEntity.ok(response); }
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) { response.put("success", false); response.put("message", "User not found"); return ResponseEntity.ok(response); }
            User user = userOpt.get();
            if (profileData.containsKey("name")) user.setName((String) profileData.get("name"));
            if (profileData.containsKey("age")) user.setAge(((Number) profileData.get("age")).intValue());
            if (profileData.containsKey("gender")) user.setGender((String) profileData.get("gender"));
            if (profileData.containsKey("height")) user.setHeight(((Number) profileData.get("height")).doubleValue());
            if (profileData.containsKey("weight")) user.setWeight(((Number) profileData.get("weight")).doubleValue());
            if (profileData.containsKey("activityLevel")) user.setActivityLevel((String) profileData.get("activityLevel"));
            // Recalculate targets after profile update
            user.setTargetCalories((int) user.getDailyCalorieRequirement());
            user.setTargetProtein(CalculationUtils.roundToTwoDecimals(user.getWeight() * 1.6));
            user.setTargetCarbs(CalculationUtils.roundToTwoDecimals((user.getTargetCalories() * 0.5) / 4));
            user.setTargetFat(CalculationUtils.roundToTwoDecimals((user.getTargetCalories() * 0.25) / 9));
            userRepository.save(user);
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", user.getName());
            userMap.put("username", user.getUsername());
            userMap.put("weight", user.getWeight());
            userMap.put("height", user.getHeight());
            userMap.put("age", user.getAge());
            userMap.put("gender", user.getGender());
            userMap.put("activityLevel", user.getActivityLevel());
            userMap.put("targetCalories", user.getTargetCalories());
            userMap.put("targetProtein", CalculationUtils.roundToTwoDecimals(user.getTargetProtein()));
            response.put("success", true);
            response.put("message", "Profile updated successfully");
            response.put("user", userMap);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false); response.put("message", "Error updating profile: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Get insights for logged-in user
     */
    @GetMapping("/api/insights")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getInsights(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.ok(response);
            }
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            List<Meal> todayMeals = mealRepository.findByUserAndDate(user, LocalDate.now());
            
            double totalCalories = CalculationUtils.roundToTwoDecimals(todayMeals.stream().mapToDouble(Meal::getCalories).sum());
            double totalProtein = CalculationUtils.roundToTwoDecimals(todayMeals.stream().mapToDouble(Meal::getProtein).sum());
            double totalCarbs = CalculationUtils.roundToTwoDecimals(todayMeals.stream().mapToDouble(Meal::getCarbs).sum());
            double totalFat = CalculationUtils.roundToTwoDecimals(todayMeals.stream().mapToDouble(Meal::getFat).sum());
            
            // Calculate progress percentages
            double caloriesProgress = CalculationUtils.calculateProgressPercentage(totalCalories, user.getTargetCalories());
            double proteinProgress = CalculationUtils.calculateProgressPercentage(totalProtein, user.getTargetProtein());
            double carbsProgress = CalculationUtils.calculateProgressPercentage(totalCarbs, user.getTargetCarbs());
            double fatProgress = CalculationUtils.calculateProgressPercentage(totalFat, user.getTargetFat());
            
            List<String> insights = new ArrayList<>();
            
            // Protein insights
            if (proteinProgress < 50) {
                insights.add("Your protein intake is quite low today. Consider adding lean meats, fish, eggs, or legumes to reach your target.");
            } else if (proteinProgress < 80) {
                insights.add("You're getting close to your protein goal. Add a protein-rich snack to hit your target!");
            }
            
            // Calorie insights
            if (caloriesProgress > 110) {
                insights.add("You've exceeded your calorie target. Focus on lighter meals and portion control tomorrow.");
            } else if (caloriesProgress > 100) {
                insights.add("You're slightly over your calorie target. Consider a light evening snack or skip dessert.");
            } else if (caloriesProgress < 50) {
                insights.add("You're below 50% of your calorie target. Make sure to eat enough to fuel your body properly.");
            } else if (caloriesProgress < 80) {
                insights.add("You're making good progress on calories. Add a balanced meal to reach your target.");
            }
            
            // Carbs insights
            if (carbsProgress < 50) {
                insights.add("Your carb intake is low. Include whole grains, fruits, or vegetables for balanced energy.");
            } else if (carbsProgress > 120) {
                insights.add("Your carb intake is high. Focus on complex carbs and reduce simple sugars.");
            }
            
            // Fat insights
            if (fatProgress < 50) {
                insights.add("You're low on healthy fats. Add nuts, avocado, or olive oil to your meals.");
            } else if (fatProgress > 120) {
                insights.add("Your fat intake is high. Choose lean protein sources and reduce fried foods.");
            }
            
            // Goal-specific insights
            if (user.getGoalType() != null) {
                if (user.getGoalType().equals("lose_weight") && caloriesProgress > 90) {
                    insights.add("For weight loss, try to stay closer to your calorie target. Consider portion control.");
                } else if (user.getGoalType().equals("gain_weight") && caloriesProgress < 80) {
                    insights.add("For weight gain, you need more calories. Add nutrient-dense foods to your meals.");
                }
            }
            
            // Progress-based insights
            if (caloriesProgress >= 90 && caloriesProgress <= 110 && 
                proteinProgress >= 80 && carbsProgress >= 80 && fatProgress >= 80) {
                insights.add("Excellent nutrition balance today! You're hitting all your targets.");
            } else if (caloriesProgress >= 80 && caloriesProgress <= 120 && 
                       proteinProgress >= 70 && carbsProgress >= 70 && fatProgress >= 70) {
                insights.add("Good job on your nutrition today! You're well-balanced and on track.");
            }
            
            // Meal frequency insights
            if (todayMeals.size() < 3 && !todayMeals.isEmpty()) {
                insights.add("Consider eating more frequent, smaller meals throughout the day for better energy.");
            } else if (todayMeals.size() > 6) {
                insights.add("You're eating frequently. Ensure your portions are appropriate for your goals.");
            }
            
            if (insights.isEmpty()) {
                insights.add("Great progress! You're on track with your nutrition goals.");
            }
            
            response.put("success", true);
            response.put("insights", insights);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Get insights error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Error generating insights: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Get motivational quote
     */
    @GetMapping("/api/motivational-quote")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMotivationalQuote(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.ok(response);
            }
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            
            // Check if user needs a new quote for today
            String newQuote = motivationalQuotesService.getTodaysQuote(user.getLastQuoteDate());
            
            if (newQuote != null) {
                user.setMotivationalQuote(newQuote);
                user.setLastQuoteDate(LocalDate.now());
                userRepository.save(user);
            }
            
            response.put("success", true);
            response.put("quote", user.getMotivationalQuote());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Motivational quote error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Error getting motivational quote: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Get user profile data
     */
    @GetMapping("/api/user/profile")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserProfile(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.ok(response);
            }
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            
            response.put("success", true);
            
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("name", user.getName());
            userMap.put("weight", user.getWeight());
            userMap.put("height", user.getHeight());
            userMap.put("age", user.getAge());
            userMap.put("gender", user.getGender());
            userMap.put("activityLevel", user.getActivityLevel());
            userMap.put("targetCalories", user.getTargetCalories());
            userMap.put("targetProtein", CalculationUtils.roundToTwoDecimals(user.getTargetProtein()));
            userMap.put("targetCarbs", CalculationUtils.roundToTwoDecimals(user.getTargetCarbs()));
            userMap.put("targetFat", CalculationUtils.roundToTwoDecimals(user.getTargetFat()));
            userMap.put("goalType", user.getGoalType());
            userMap.put("goalWeight", user.getGoalWeight());
            userMap.put("goalWeeks", user.getGoalWeeks());
            response.put("user", userMap);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Get user profile error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Error fetching user profile: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Get chart data for last 7 days
     */
    @GetMapping("/api/chart-data")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getChartData(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.ok(response);
            }
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            LocalDate today = LocalDate.now();
            LocalDate weekStart = today.minusDays(6);
            
            // Get meals for the last 7 days
            List<Meal> weeklyMeals = mealRepository.findByUserAndDateBetween(user, weekStart, today);
            
            // Prepare chart data
            List<String> labels = new ArrayList<>();
            List<Double> caloriesData = new ArrayList<>();
            List<Double> proteinData = new ArrayList<>();
            List<Double> carbsData = new ArrayList<>();
            List<Double> fatData = new ArrayList<>();
            
            // Initialize with zeros for all days
            for (int i = 6; i >= 0; i--) {
                LocalDate date = today.minusDays(i);
                labels.add(date.getDayOfMonth() + "/" + (date.getMonthValue() < 10 ? "0" + date.getMonthValue() : date.getMonthValue()));
                
                // Calculate totals for this specific day
                List<Meal> dayMeals = weeklyMeals.stream()
                    .filter(meal -> meal.getDate().equals(date))
                    .toList();
                
                double dayCalories = CalculationUtils.roundToTwoDecimals(dayMeals.stream().mapToDouble(Meal::getCalories).sum());
                double dayProtein = CalculationUtils.roundToTwoDecimals(dayMeals.stream().mapToDouble(Meal::getProtein).sum());
                double dayCarbs = CalculationUtils.roundToTwoDecimals(dayMeals.stream().mapToDouble(Meal::getCarbs).sum());
                double dayFat = CalculationUtils.roundToTwoDecimals(dayMeals.stream().mapToDouble(Meal::getFat).sum());
                
                caloriesData.add(dayCalories);
                proteinData.add(dayProtein);
                carbsData.add(dayCarbs);
                fatData.add(dayFat);
            }
            
            // Calculate weekly totals and averages
            double totalWeeklyCalories = caloriesData.stream().mapToDouble(Double::doubleValue).sum();
            double totalWeeklyProtein = proteinData.stream().mapToDouble(Double::doubleValue).sum();
            double totalWeeklyCarbs = carbsData.stream().mapToDouble(Double::doubleValue).sum();
            double totalWeeklyFat = fatData.stream().mapToDouble(Double::doubleValue).sum();
            
            int daysWithData = (int) caloriesData.stream().mapToDouble(Double::doubleValue).filter(val -> val > 0).count();
            
            response.put("success", true);
            
            // Create chart data map using HashMap to avoid Map.of() limit
            Map<String, Object> chartDataMap = new HashMap<>();
            chartDataMap.put("labels", labels);
            chartDataMap.put("calories", caloriesData);
            chartDataMap.put("protein", proteinData);
            chartDataMap.put("carbs", carbsData);
            chartDataMap.put("fat", fatData);
            chartDataMap.put("targetCalories", user.getTargetCalories());
            chartDataMap.put("targetProtein", CalculationUtils.roundToTwoDecimals(user.getTargetProtein()));
            chartDataMap.put("targetCarbs", CalculationUtils.roundToTwoDecimals(user.getTargetCarbs()));
            chartDataMap.put("targetFat", CalculationUtils.roundToTwoDecimals(user.getTargetFat()));
            
            Map<String, Object> weeklyTotals = new HashMap<>();
            weeklyTotals.put("calories", CalculationUtils.roundToTwoDecimals(totalWeeklyCalories));
            weeklyTotals.put("protein", CalculationUtils.roundToTwoDecimals(totalWeeklyProtein));
            weeklyTotals.put("carbs", CalculationUtils.roundToTwoDecimals(totalWeeklyCarbs));
            weeklyTotals.put("fat", CalculationUtils.roundToTwoDecimals(totalWeeklyFat));
            chartDataMap.put("weeklyTotals", weeklyTotals);
            
            Map<String, Object> weeklyAverages = new HashMap<>();
            weeklyAverages.put("calories", CalculationUtils.calculateAverageCalories(totalWeeklyCalories, 7));
            weeklyAverages.put("protein", CalculationUtils.calculateAverageProtein(totalWeeklyProtein, 7));
            weeklyAverages.put("carbs", CalculationUtils.calculateAverageCarbs(totalWeeklyCarbs, 7));
            weeklyAverages.put("fat", CalculationUtils.calculateAverageFat(totalWeeklyFat, 7));
            chartDataMap.put("weeklyAverages", weeklyAverages);
            
            chartDataMap.put("daysWithData", daysWithData);
            response.put("chartData", chartDataMap);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Get chart data error: " + e.getMessage());
            response.put("success", false);
            response.put("message", "Error fetching chart data: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Handle logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
    /**
     * Get simple goal data
     */
    @GetMapping("/api/goal")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getGoal(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.ok(response);
            }
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            
            response.put("success", true);
            response.put("targetCalories", user.getTargetCalories());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching goal: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    /**
     * Update simple goal data
     */
    @PutMapping("/api/goal")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateGoal(@RequestBody Map<String, Object> goalData, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                response.put("success", false);
                response.put("message", "User not logged in");
                return ResponseEntity.ok(response);
            }
            
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            User user = userOpt.get();
            
            if (goalData.containsKey("targetCalories")) {
                int targetCalories = ((Number) goalData.get("targetCalories")).intValue();
                if (targetCalories > 0) {
                    user.setTargetCalories(targetCalories);
                }
            }
            
            userRepository.save(user);
            
            response.put("success", true);
            response.put("message", "Goal updated successfully");
            response.put("targetCalories", user.getTargetCalories());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating goal: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
