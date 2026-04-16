package com.nutritrack.repository;

import com.nutritrack.model.DailyTracker;
import com.nutritrack.model.Meal;
import com.nutritrack.model.FoodItem;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository layer for Meal data management
 * Follows Repository Pattern for data access abstraction
 */
public class MealRepository {
    private static MealRepository instance;
    private Map<LocalDate, DailyTracker> dailyTrackers;
    
    private MealRepository() {
        this.dailyTrackers = new HashMap<>();
    }
    
    /**
     * Singleton pattern for repository access
     */
    public static MealRepository getInstance() {
        if (instance == null) {
            instance = new MealRepository();
        }
        return instance;
    }
    
    /**
     * Get daily tracker for a specific date
     */
    public Optional<DailyTracker> findByDate(LocalDate date) {
        DailyTracker tracker = dailyTrackers.get(date);
        if (tracker == null) {
            tracker = new DailyTracker(date);
            dailyTrackers.put(date, tracker);
        }
        return Optional.of(tracker);
    }
    
    /**
     * Add food item to a specific meal type for a date
     */
    public boolean addFoodItem(String username, LocalDate date, String mealType, FoodItem foodItem) {
        Optional<DailyTracker> trackerOpt = findByDate(date);
        if (trackerOpt.isPresent()) {
            DailyTracker tracker = trackerOpt.get();
            tracker.addFoodToMeal(mealType, foodItem);
            return true;
        }
        return false;
    }
    
    /**
     * Get all meals for a specific date
     */
    public Map<String, Meal> getMealsByDate(LocalDate date) {
        Optional<DailyTracker> trackerOpt = findByDate(date);
        return trackerOpt.map(DailyTracker::getMeals).orElse(new HashMap<>());
    }
    
    /**
     * Get all daily trackers
     */
    public Map<LocalDate, DailyTracker> findAll() {
        return new HashMap<>(dailyTrackers);
    }
    
    /**
     * Get daily trackers for a date range
     */
    public Map<LocalDate, DailyTracker> findByDateRange(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, DailyTracker> result = new HashMap<>();
        for (LocalDate currentDate = startDate; !currentDate.isAfter(endDate); currentDate = currentDate.plusDays(1)) {
            Optional<DailyTracker> trackerOpt = findByDate(currentDate);
            if (trackerOpt.isPresent()) {
                result.put(currentDate, trackerOpt.get());
            }
        }
        return result;
    }
    
    /**
     * Delete daily tracker for a specific date
     */
    public boolean deleteByDate(LocalDate date) {
        return dailyTrackers.remove(date) != null;
    }
    
    /**
     * Clear all data (for testing)
     */
    public void clearAll() {
        dailyTrackers.clear();
    }
}
