package com.nutritrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main Spring Boot application class for NutriTrack web application
 * 
 * Features:
 * - Spring Boot web application with REST APIs
 * - Thymeleaf templating for frontend
 * - H2 in-memory database
 * - Modern nutrition tracking system
 * 
 * @author NutriTrack Team
 * @version 3.0 (Web Edition)
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.nutritrack.repository")
public class NutriTrackApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(NutriTrackApplication.class, args);
        System.out.println("==============================================");
        System.out.println("   NutriTrack Web Application Started!");
        System.out.println("==============================================");
        System.out.println("Access the application at: http://localhost:8080");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("==============================================");
    }
}
