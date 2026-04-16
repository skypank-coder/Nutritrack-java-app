package com.nutritrack.repository;

import com.nutritrack.model.Meal;
import com.nutritrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for Meal entity
 */
@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    
    List<Meal> findByUserAndDate(User user, LocalDate date);
    
    List<Meal> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT m FROM Meal m WHERE m.user = :user AND CAST(m.date AS LocalDate) = :date")
    List<Meal> findMealsByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT SUM(m.calories) FROM Meal m WHERE m.user = :user AND CAST(m.date AS LocalDate) = :date")
    Double getTotalCaloriesByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT SUM(m.protein) FROM Meal m WHERE m.user = :user AND CAST(m.date AS LocalDate) = :date")
    Double getTotalProteinByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT SUM(m.carbs) FROM Meal m WHERE m.user = :user AND CAST(m.date AS LocalDate) = :date")
    Double getTotalCarbsByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT SUM(m.fat) FROM Meal m WHERE m.user = :user AND CAST(m.date AS LocalDate) = :date")
    Double getTotalFatByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
    
    @Query("SELECT m FROM Meal m WHERE m.user = :user ORDER BY m.date DESC")
    List<Meal> findByUserOrderByDateDesc(@Param("user") User user);
    
    @Query("SELECT m FROM Meal m WHERE m.user = :user AND CAST(m.date AS LocalDate) = CURRENT_DATE")
    List<Meal> findTodayMealsByUser(@Param("user") User user);
}
