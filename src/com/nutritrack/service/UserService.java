package com.nutritrack.service;

import com.nutritrack.model.User;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static UserService instance;
    private Map<String, User> users;
    private User currentUser;
    
    private UserService() {
        users = new HashMap<>();
    }
    
    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    
    public boolean registerUser(String username, String password, String name, double height, double weight, int age, String gender, String activityLevel) {
        if (users.containsKey(username)) {
            return false;
        }
        
        User user = new User(username, password, name, height, weight, age, gender, activityLevel);
        users.put(username, user);
        return true;
    }
    
    public boolean loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }
    
    public void logout() {
        currentUser = null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean updateCurrentUser(String name, double height, double weight, int age, String gender, String activityLevel) {
        if (currentUser == null) {
            return false;
        }
        
        currentUser.setName(name);
        currentUser.setHeight(height);
        currentUser.setWeight(weight);
        currentUser.setAge(age);
        currentUser.setGender(gender);
        currentUser.setActivityLevel(activityLevel);
        
        return true;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
