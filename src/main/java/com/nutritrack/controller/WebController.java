package com.nutritrack.controller;

import com.nutritrack.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Web controller for serving HTML pages
 */
// Superseded by PageController - disabled to avoid route conflicts
// @Controller
public class WebController {
    
    /**
     * Home page - redirect to login if not authenticated
     */
    @GetMapping("/")
    public String home(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/dashboard";
        }
        return "redirect:/login";
    }
    
    /**
     * Login page
     */
    @GetMapping("/login")
    public String login(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }
    
    /**
     * Dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "dashboard";
    }
    
    /**
     * Add meal page
     */
    @GetMapping("/add-meal")
    public String addMeal(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "add-meal";
    }
    
    /**
     * Reports page
     */
    @GetMapping("/reports")
    public String reports(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "reports";
    }
    
    /**
     * BMI Calculator page
     */
    @GetMapping("/bmi-calculator")
    public String bmiCalculator(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        return "bmi-calculator";
    }
    
    /**
     * Logout
     */
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
