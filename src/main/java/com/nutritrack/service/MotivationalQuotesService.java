package com.nutritrack.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * Service for providing motivational quotes to users
 */
@Service
public class MotivationalQuotesService {
    
    private final Random random = new Random();
    
    private final List<String> quotes = List.of(
        "Consistency beats perfection",
        "Small progress is still progress",
        "Every healthy choice is a victory",
        "Your future self will thank you",
        "One day at a time, one meal at a time",
        "You're stronger than you think",
        "Progress, not perfection",
        "Healthy habits build healthy lives",
        "Trust the process",
        "Your health is your wealth",
        "Nourish your body, fuel your dreams",
        "Every step counts",
        "Believe in yourself",
        "Stay focused, stay strong",
        "You've got this!",
        "Make today count",
        "Healthy choices, happy life",
        "Your body deserves the best",
        "Invest in your health",
        "Stronger every day"
    );
    
    /**
     * Get a random motivational quote
     */
    public String getRandomQuote() {
        return quotes.get(random.nextInt(quotes.size()));
    }
    
    /**
     * Get today's quote for a user (checks if they need a new quote)
     */
    public String getTodaysQuote(LocalDate lastQuoteDate) {
        LocalDate today = LocalDate.now();
        
        // If user has no quote date or it's not today, give them a new quote
        if (lastQuoteDate == null || !lastQuoteDate.equals(today)) {
            return getRandomQuote();
        }
        
        // Return null to indicate they should keep their current quote
        return null;
    }
    
    /**
     * Get a quote based on user's current progress
     */
    public String getProgressBasedQuote(double caloriesProgress, double proteinProgress) {
        if (caloriesProgress >= 90 && proteinProgress >= 90) {
            return "Amazing job hitting your goals today!";
        } else if (caloriesProgress >= 75 && proteinProgress >= 75) {
            return "You're doing great! Keep pushing!";
        } else if (caloriesProgress >= 50 || proteinProgress >= 50) {
            return "Halfway there! You've got this!";
        } else if (caloriesProgress < 25 && proteinProgress < 25) {
            return "Every journey starts with a single step!";
        } else {
            return "Stay focused on your goals!";
        }
    }
}
