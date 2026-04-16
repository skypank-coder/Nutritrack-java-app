# NutriTrack Web Application - Quick Start Guide

## **URL CHANGED TO PORT 8081** 

The application now runs on: **http://localhost:8081**

---

## **FILES CLEANED UP**

### **Removed Files:**
- `NutriTrackFXApp.java` - Old JavaFX application
- `ConsoleDemo.java` - Console demo
- `InteractiveDemo.java` - Interactive demo
- `test/` - Test directory
- `ui/` - Old JavaFX UI directory
- `compile_and_run.bat` - Old compilation script
- `compile_javafx.bat` - JavaFX compilation script
- `run_architecture_test.bat` - Test runner
- `run_javafx.bat` - JavaFX runner
- `setup_javafx.md` - JavaFX setup guide

### **Remaining Files:**
- `pom.xml` - Maven configuration (Spring Boot)
- `run_web_app.bat` - New run script
- `src/main/` - Spring Boot application structure
- `README.md` - Updated documentation

---

## **HOW TO RUN**

### **Option 1: Easy Way (Recommended)**
1. **Double-click** `run_web_app.bat`
2. **Wait** for Maven to download dependencies
3. **Open browser** to http://localhost:8081

### **Option 2: Manual Way**
1. **Install Maven** if not installed
2. **Open command prompt** in project directory
3. **Run**: `mvn spring-boot:run`
4. **Open browser** to http://localhost:8081

### **Option 3: IDE Way**
1. **Import project** into IntelliJ IDEA or Eclipse
2. **Run** `NutriTrackApplication.java`
3. **Open browser** to http://localhost:8081

---

## **WHAT YOU'LL SEE**

### **Login Page**
- Modern login/register interface
- Professional health-themed design
- Mobile responsive

### **Dashboard**
- Real-time nutrition tracking
- BMI/BMR statistics
- Progress bars and charts

### **Features**
- **Add Meal**: Track food intake with 27+ food items
- **Reports**: Comprehensive nutrition analysis
- **BMI Calculator**: Visual health metrics

---

## **TROUBLESHOOTING**

### **Port 8081 Not Working?**
Try these alternative ports:
- **8082**: Change `server.port=8082` in `application.properties`
- **8083**: Change `server.port=8083` in `application.properties`
- **9090**: Change `server.port=9090` in `application.properties`

### **Maven Issues?**
1. **Check Java version**: Must be Java 17+
2. **Install Maven**: Download from https://maven.apache.org/download.cgi
3. **Add to PATH**: Set MAVEN_HOME environment variable

### **Browser Issues?**
- **Chrome**: http://localhost:8081
- **Firefox**: http://localhost:8081
- **Edge**: http://localhost:8081

---

## **DATABASE ACCESS**

**H2 Console**: http://localhost:8081/h2-console
- **JDBC URL**: `jdbc:h2:mem:nutritrackdb`
- **Username**: `sa`
- **Password**: `password`

---

## **SUCCESS INDICATORS**

### **You'll know it's working when:**
1. **Console shows**: "NutriTrack Web Application Started!"
2. **Browser opens** to login page
3. **No error messages** in console
4. **H2 console** is accessible

### **If you see:**
- **"Tomcat started on port(s): 8081"** - SUCCESS!
- **"Application started successfully"** - SUCCESS!
- **Login page loads** - SUCCESS!

---

## **NEXT STEPS**

1. **Register** a new account
2. **Login** with your credentials
3. **Add your first meal**
4. **Check your dashboard**
5. **Explore all features**

---

## **NEED HELP?**

### **Common Solutions:**
- **Port conflict**: Change port number in `application.properties`
- **Maven missing**: Install Maven from official site
- **Java version**: Ensure Java 17+ is installed
- **Browser cache**: Clear cache and refresh

### **Quick Test:**
```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Run application
mvn spring-boot:run
```

---

**The NutriTrack web application is ready to run on http://localhost:8081!**
