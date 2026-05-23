# 🌿 QuietPlace

## 📌 Overview
QuietPlace is a full-stack web application designed to help users find quiet environments for studying, working, or relaxing based on noise level preferences.

The system uses an interactive map, filtering system, and recommendation engine to suggest suitable places while also allowing users to contribute noise data for improved accuracy.

---

## 🎯 Project Objective
The goal of QuietPlace is to:
- Help users discover low-noise environments
- Improve productivity and comfort
- Provide real-time community-driven noise data
- Demonstrate full-stack development using Spring Boot

---

## ⚙️ Features

### 👤 User Authentication
- Secure registration and login system
- Session-based authentication
- BCrypt password encryption

---

### 🗺️ Place Exploration
- Interactive map using Leaflet.js
- Search places by:
  - Noise level
  - Purpose (study, work, relaxation)
  - Amenities
  - Distance (Haversine formula)
- List + map dual view

---

### ⭐ Favorites System
- Save and manage favorite places
- Prevent duplicate entries
- Personalized user experience

---

### 👤 User Profile & Preferences
- View and update profile
- Set preferred noise level
- Define default search radius
- Store user preferences

---

### 📊 Noise Reading System
- Users submit noise and crowdedness data
- Admin validation required
- Daily submission limit (5 per user)
- Aggregated noise statistics per place

---

### 🛠️ Admin Dashboard
- Manage users and places
- Verify noise submissions
- View system statistics
- Role-based access control (RBAC)

---

## 🧠 System Architecture

QuietPlace follows the **Spring Boot MVC architecture**:

### 📌 Layers:

**1. Presentation Layer**
- Thymeleaf templates
- UI pages (login, home, admin dashboard)

**2. Controller Layer**
- Handles HTTP requests
- Example: AuthController, PlaceController

**3. Service Layer**
- Business logic
- Recommendation system
- Noise calculations

**4. Repository Layer**
- Database access using Spring Data JPA

**5. Database Layer**
- Users
- Places
- Favorites
- Noise Readings

---

## 🔐 Security Features
- Session-based authentication
- Role-Based Access Control (RBAC)
- BCrypt password hashing
- Method-level security using `@PreAuthorize`
- Input validation and secure API handling

---

## 🛠️ Tech Stack
- Java
- Spring Boot
- Spring MVC
- Spring Security
- MySQL
- Thymeleaf
- Leaflet.js (Maps)
- REST APIs

---

## 📈 Key Algorithms & Concepts
- Haversine Formula (distance calculation)
- Recommendation system logic
- Many-to-many relationships
- Aggregation of user-generated data
- MVC architecture pattern
- Role-based authorization (RBAC)

---

## 👥 User Roles

### 👤 Regular User
- Search places
- Save favorites
- Submit noise readings
- Manage profile

### 🛡️ Admin
- Manage users
- Add/edit/delete places
- Verify noise readings
- View analytics

---

## 🚀 How to Run the Project

1. Clone repository:
```bash
git clone https://github.com/your-username/quietplace.git
