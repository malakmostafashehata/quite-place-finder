package org.example.quiet_place.Controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.quiet_place.model.Place;
import org.example.quiet_place.model.User;
import org.example.quiet_place.service.PlaceService;
import org.example.quiet_place.service.UserService;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/api/admin")
public class AdminApiController {

    private final UserService userService;
    private final PlaceService placeService;

    public AdminApiController(UserService userService, PlaceService placeService) {
        this.userService = userService;
        this.placeService = placeService;
    }

    private boolean isAdmin(HttpServletRequest request) {
        String role = (String) request.getSession().getAttribute("role");
        String email = (String) request.getSession().getAttribute("email");
        System.out.println("=== ADMIN CHECK ===");
        System.out.println("Role from session: " + role);
        System.out.println("Email from session: " + email);

        boolean isAdmin = "admin".equals(role) && "admin@gmail.com".equals(email);
        System.out.println("Is Admin: " + isAdmin);

        return isAdmin;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/places")
    public ResponseEntity<?> getAllPlaces(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @PostMapping("/places")
    public ResponseEntity<?> addPlace(@RequestBody Place place, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        return ResponseEntity.ok(placeService.addPlace(place));
    }
    @PutMapping("/places/{id}")
    public ResponseEntity<?> updatePlace(@PathVariable Long id, @RequestBody Place place, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        return ResponseEntity.ok(placeService.updatePlace(id, place));
    }

    @DeleteMapping("/places/{id}")
    public ResponseEntity<?> deletePlace(@PathVariable Long id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required");
        }
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}