# NutriTrack – Smart Nutrition & Health Tracker

A full-stack web application built with Spring Boot for tracking daily nutrition, managing health goals, and monitoring dietary habits with real-time data visualization.

> **Status: Working & Verified** — Built with Maven 3.9.14 + Java 17, confirmed running on `http://localhost:8080`

---

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [OOP Concepts Used](#oop-concepts-used)
- [System Flow](#system-flow)
- [API Reference](#api-reference)
- [Database](#database)
- [How to Run](#how-to-run)
- [Testing](#testing)
- [Future Enhancements](#future-enhancements)
- [Author](#author)

---

## Project Overview

NutriTrack is a full-stack web application that enables users to track their daily food intake, monitor macronutrient consumption, set calorie goals, and analyze weekly nutrition trends. The application provides a real-time dashboard that updates immediately after each meal entry, giving users an accurate picture of their nutritional status throughout the day.

Key capabilities include:

- Tracking calories, protein, carbohydrates, and fat per meal across 27 food items
- Calculating BMI and BMR based on user profile data (height, weight, age, gender, activity level)
- Visualizing daily and weekly nutrition trends through interactive Chart.js charts
- Setting and monitoring personalized calorie goals with live progress tracking
- Generating detailed 7-day nutrition reports from persistent H2 database records
- Session-based authentication — all data is user-specific and isolated

---

## Features

| Feature | Description |
|---|---|
| **User Authentication** | Register and login with server-side session management (`userId` stored in `HttpSession`) |
| **Meal Tracking** | Add meals by selecting food + entering grams; nutrition auto-calculated from built-in food database |
| **Real-time Dashboard** | Fetches live data from `/api/nutrition/summary` on every page load |
| **Dual Progress Bars** | Top progress bar and Goal section both sourced from the same API — always in sync |
| **Goal Setting** | Inline edit form on dashboard saves `targetCalories` via `PUT /api/goal` |
| **Goals Page** | Full goal management — target calories, protein, carbs, fat, goal type, target weight, duration |
| **Profile Page** | View and update name, age, weight, height, gender, activity level; targets auto-recalculate |
| **Daily Reports** | Per-day breakdown table + 4 charts (calorie trend, nutrition breakdown, protein, carbs/fat) |
| **Weekly Analysis** | `weeklyAvgCalories = totalCaloriesLast7Days / 7` — always divides by 7 |
| **BMI Calculator** | Client-side `weight / (height_m²)`, result rounded to 2 decimals, category + health tips |
| **Dynamic Charts** | Pie chart (today's macros) + line chart (7-day calorie trend vs target) on dashboard |
| **Persistent Storage** | H2 file-based at `./data/nutritrack-db` — data survives restarts |

---

## Tech Stack

**Backend**
- Java 17
- Spring Boot 3.2.0
- Spring MVC (`@Controller`, `@ResponseBody`)
- Spring Data JPA / Hibernate
- Spring Security (CSRF disabled for `/api/**`, session-based auth)
- H2 Database (file-based persistence)
- Maven 3.9.14

**Frontend**
- HTML5 / CSS3
- JavaScript (Fetch API, `async/await`, `Promise.all`)
- Bootstrap 5.3
- Chart.js
- Font Awesome 6.4

---

## Project Structure

```
src/main/
├── java/com/nutritrack/
│   ├── config/
│   │   └── SecurityConfig.java              # Spring Security — permits all routes, disables CSRF for /api/**
│   ├── controller/
│   │   └── NutriTrackController.java        # Single active controller — all page routes + REST APIs
│   ├── model/
│   │   ├── User.java                        # JPA entity: credentials, profile, goals, quote fields
│   │   ├── Meal.java                        # JPA entity: linked to User via @ManyToOne
│   │   └── Food.java                        # Plain POJO: food item with per-100g nutrition values
│   ├── repository/
│   │   ├── UserRepository.java              # JpaRepository<User, Long> + findByUsername, existsByUsername
│   │   └── MealRepository.java              # JpaRepository<Meal, Long> + date/user filtered queries
│   ├── service/
│   │   ├── NutritionService.java            # Active: calculateNutrition(food, grams), 27-item food DB
│   │   └── MotivationalQuotesService.java   # Active: daily quote rotation per user
│   └── util/
│       ├── CalculationUtils.java            # roundToTwoDecimals, calculateAverage*, calculateProgress%
│       ├── BMICalculator.java               # BMI formula + category enum (UNDERWEIGHT/NORMAL/OVERWEIGHT/OBESE)
│       ├── BMRCalculator.java               # Harris-Benedict BMR + activity multiplier → daily calorie target
│       └── ValidationUtil.java             # Input validation (username, password, height, weight, age)
│
└── resources/
    ├── templates/                           # 7 active Thymeleaf pages
    │   ├── login.html                       # Login + Register tabs
    │   ├── dashboard-working.html           # Main dashboard with charts, progress, meals table
    │   ├── add-meal.html                    # Meal entry form → redirects to /dashboard on success
    │   ├── reports.html                     # 4 charts + per-day table, all from real API data
    │   ├── goals.html                       # Full goal form (calories, macros, goal type, weight, weeks)
    │   ├── profile.html                     # Profile edit form with BMI/BMR stats display
    │   └── bmi-calculator.html              # Standalone BMI tool with history (localStorage)
    ├── static/
    │   ├── css/style.css
    │   └── js/app.js                        # Shared NutriTrack JS utilities
    └── application.properties
```

---

## OOP Concepts Used

### Encapsulation
All entity fields in `User` and `Meal` are declared `private` and accessed exclusively through public getters and setters. For example, `targetCalories` can only be modified through `setTargetCalories()`, protecting internal state from direct mutation.

### Abstraction
`NutritionService.calculateNutrition(foodName, grams)` hides the food database lookup and per-100g scaling formula from the controller. The controller simply passes food name and grams and receives a ready-to-use nutrition map — it has no knowledge of how the calculation works internally.

### Inheritance
`UserRepository` and `MealRepository` extend `JpaRepository<T, ID>`, inheriting `save()`, `findById()`, `findAll()`, `delete()`, and pagination support without writing any implementation code. Custom JPQL queries are added on top via `@Query` annotations.

### Polymorphism
`CalculationUtils` provides type-specific static methods (`calculateAverageCalories`, `calculateAverageProtein`, `calculateAverageCarbs`, `calculateAverageFat`) that apply the same averaging formula across different nutrient types. Spring's `@Autowired` dependency injection also demonstrates polymorphism — `NutriTrackController` depends on `NutritionService` by type, not by concrete implementation.

### Separation of Concerns
Strict three-layer architecture:
- **Controller** — handles HTTP only, no business logic
- **Service** — all calculations and food database logic
- **Repository** — all database interactions via JPA

Frontend templates display values returned by the backend API. No nutrition calculations are performed in JavaScript.

---

## System Flow

```
User Registers / Logs In
        │
        ▼
POST /api/login → session.setAttribute("userId", user.getId())
        │
        ▼
Redirect to /dashboard
        │
        ▼
DOMContentLoaded → Promise.all([/api/nutrition/summary, /api/meal/all])
        │
        ▼
Backend: queries meals WHERE user_id = ? AND date = TODAY
         sums calories/protein/carbs/fat
         computes progress = (todayCalories / targetCalories) * 100
        │
        ▼
Dashboard updates: stat cards, top progress bar, goal section (same %), charts, meals table
        │
        ▼
User adds meal → POST /api/meal/add → saved to DB → redirect to /dashboard
        │
        ▼
Dashboard reloads → fresh data from DB → all values updated
        │
        ▼
Reports page → GET /api/chart-data → 7-day per-day breakdown → 4 charts + table
```

---

## API Reference

All APIs use `HttpSession` to identify the logged-in user. No token required.

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/register` | Register new user (form params) |
| `POST` | `/api/login` | Login, creates session |
| `GET` | `/logout` | Invalidates session, redirects to `/` |
| `GET` | `/api/nutrition/summary` | Today's totals + weekly avgs + progress % |
| `GET` | `/api/meal/all` | Today's meals for logged-in user |
| `POST` | `/api/meal/add` | Add a meal (JSON body) |
| `GET` | `/api/meal/foods` | List of 26 available food names |
| `GET` | `/api/goal` | Get current `targetCalories` |
| `PUT` | `/api/goal` | Update `targetCalories` |
| `GET` | `/api/goals` | Get all goal fields (calories, macros, type, weight, weeks) |
| `PUT` | `/api/goals` | Update all goal fields |
| `GET` | `/api/profile` | Get full user profile |
| `PUT` | `/api/profile` | Update profile; auto-recalculates macro targets |
| `GET` | `/api/user/profile` | Alias for profile (used by dashboard welcome name) |
| `GET` | `/api/chart-data` | 7-day per-day data for all charts |
| `GET` | `/api/insights` | Nutrition insights based on today's progress |
| `GET` | `/api/motivational-quote` | Daily quote (rotates once per day per user) |

---

## Database

NutriTrack uses **H2 in file-based mode** — all data persists across application restarts.

**Configuration** (`application.properties`):
```properties
spring.datasource.url=jdbc:h2:file:./data/nutritrack-db
spring.jpa.hibernate.ddl-auto=update
```

The database file is created at `./data/nutritrack-db.mv.db` relative to the working directory when the app starts.

**`users` table** — stores credentials, profile data (height, weight, age, gender, activityLevel), goal fields (targetCalories, targetProtein, targetCarbs, targetFat, goalType, goalWeight, goalWeeks), and motivational quote fields (motivationalQuote, lastQuoteDate).

**`meals` table** — stores each meal entry with a foreign key (`user_id`) to the user, along with foodName, mealType, grams, date, and pre-calculated nutrition values (calories, protein, carbs, fat).

H2 console available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/nutritrack-db`, no password).

---

## How to Run

**Prerequisites**
- Java 17 (JDK, not JRE)
- Maven 3.6+ — download from [maven.apache.org](https://maven.apache.org/download.cgi) and extract

**Steps**

```bash
# 1. Clone the repository
git clone <repository-url>
cd Nutritrack-main

# 2. Build the project (downloads dependencies on first run)
mvn clean install

# 3a. Run via Maven
mvn spring-boot:run

# 3b. OR run the built JAR directly
java -jar target/nutritrack-1.0.0.jar
```

**If `mvn` is not on PATH**, use the full path to your Maven installation:
```bash
# Windows example
"C:\Users\<you>\Downloads\apache-maven-3.9.14\bin\mvn.cmd" clean install
"C:\Program Files\Java\jdk-17\bin\java.exe" -jar target\nutritrack-1.0.0.jar
```

**Access the application**

| URL | Description |
|---|---|
| `http://localhost:8080` | Login / Registration page |
| `http://localhost:8080/dashboard` | Main dashboard |
| `http://localhost:8080/add-meal.html` | Add a meal |
| `http://localhost:8080/reports.html` | Weekly reports |
| `http://localhost:8080/goals` | Goal management |
| `http://localhost:8080/profile` | User profile |
| `http://localhost:8080/bmi` | BMI calculator |
| `http://localhost:8080/h2-console` | H2 database console |

---

## Testing

Manual testing verified across all core user flows:

- **Login / Register** — session created on login, `userId` stored server-side, all protected routes redirect to `/` if session is missing
- **Meal tracking** — adding a meal saves to H2 with correct user FK and today's date; dashboard reloads with updated totals
- **Progress consistency** — top progress bar and goal section both read from `nutritionData.progress` returned by `/api/nutrition/summary` — no duplicate calculation, no mismatch
- **Weekly avg calories** — verified as `totalCaloriesLast7Days / 7` (always divides by 7, not by days with meals)
- **Goal update** — `PUT /api/goal` saves to DB; dashboard reloads and both progress bars reflect new target immediately
- **Profile update** — saving profile recalculates `targetCalories`, `targetProtein`, `targetCarbs`, `targetFat` using BMR + activity multiplier
- **Reports** — all 4 charts and the per-day table use real data from `/api/chart-data`; no hardcoded or mock values
- **BMI calculator** — formula `weight / (height_m²)` verified, result rounded to 2 decimal places, correct category boundaries
- **Data persistence** — meal and user data confirmed to survive application restart via file-based H2 storage
- **Build** — `mvn clean install` completes with `BUILD SUCCESS`, 24 source files compiled, 0 errors, 1 deprecation warning (non-breaking)

---

## Future Enhancements

- **AI Diet Recommendations** — recommendation engine to suggest meals based on nutritional gaps and user goals
- **Mobile Application** — React Native or Flutter client consuming the existing REST API
- **Wearable Integration** — sync step count and activity data from fitness trackers to auto-adjust daily calorie targets
- **Barcode Scanner** — camera-based food lookup to populate meal entries automatically
- **Multi-language Support** — internationalization for broader accessibility
- **Cloud Deployment** — Docker containerization and deployment to AWS with PostgreSQL replacing H2

---

## Author

**[Your Name]**  
Full-Stack Java Developer  
[GitHub Profile] | [LinkedIn Profile] | [Email]

---

*Built with Spring Boot 3.2 · Java 17 · H2 · Bootstrap 5 · Chart.js*
