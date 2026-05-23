package org.example.quiet_place.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quiet_place.dto.LoginRequest;
import org.example.quiet_place.dto.RegisterRequest;
import org.example.quiet_place.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.example.quiet_place.service.EmailService;   

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final EmailService emailService; 

    // Constructor Injection try to use @autowird
    public AuthController(UserService userService, EmailService emailService) { 
        this.userService = userService;
        this.emailService = emailService;  
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpRequest) {
        // Check email and password
        try {
            var user = userService.login(loginRequest, httpRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        response.setHeader("Location", "/login");
        response.setStatus(302);
        return ResponseEntity.ok(Map.of("message", "Logout successful", "redirect", "/login"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            var user = userService.register(registerRequest);

            // send welcome email
            emailService.sendWelcomeEmail(registerRequest.getEmail(), registerRequest.getUsername());

            return ResponseEntity.ok(Map.of("message", "Registration successful", "userId", user.getId()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }
}
