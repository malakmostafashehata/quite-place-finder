package org.example.quiet_place.Controller;

import jakarta.servlet.http.HttpSession;
import org.example.quiet_place.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private ResponseEntity<Map<String, String>> success(String message) {
        return ResponseEntity.ok(Map.of("message", message));
    }

    private ResponseEntity<Map<String, Object>> response(String message, String key, Object value) {
        return ResponseEntity.ok(Map.of("message", message, key, value));
    }

    private ResponseEntity<Map<String, Object>> data(String key, Object value) {
        return ResponseEntity.ok(Map.of(key, value));
    }

    @PutMapping("/noise-preference")
    public ResponseEntity<?> updateNoisePreference(@RequestBody Map<String, Integer> payload) {
        userService.updateNoisePreference(payload.get("noiseLevel"));
        return success("Noise preference updated");
    }

    @PutMapping("/range")
    public ResponseEntity<?> updateRange(@RequestBody Map<String, Double> payload) {
        userService.updateDefaultRange(payload.get("range"));
        return success("Range updated");
    }

    @PutMapping("/location")
    public ResponseEntity<?> updateLocation(@RequestBody Map<String, Double> payload) {
        userService.updateLocation(payload.get("latitude"), payload.get("longitude"));
        return success("Location updated");
    }

    @PutMapping("/auto-location")
    public ResponseEntity<?> toggleAutoLocation(@RequestBody Map<String, Boolean> payload) {
        userService.toggleAutoLocation(payload.get("useAutoLocation"));
        return success("Auto-location toggled");
    }

    @PostMapping("/favorites/{placeId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long placeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not logged in"));
        }

        userService.addFavorite(placeId, userId);
        return response("Added to favorites", "placeId", placeId);
    }

    @DeleteMapping("/favorites/{placeId}")
    public ResponseEntity<?> removeFavorite(@PathVariable Long placeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not logged in"));
        }
        userService.removeFavorite(placeId, userId);
        return response("Removed from favorites", "placeId", placeId);
    }

    @GetMapping("/favorites")
    public ResponseEntity<Set<Long>> getFavorites(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(userService.getUserFavorites());
    }

    @GetMapping("/favorites/{placeId}/check")
    public ResponseEntity<?> checkFavorite(@PathVariable Long placeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.ok(Map.of("isFavorite", false, "placeId", placeId));
        }
        boolean isFavorite = userService.isPlaceFavorite(placeId);
        return ResponseEntity.ok(Map.of("isFavorite", isFavorite, "placeId", placeId));
    }

    @GetMapping("/favorites/count")
    public ResponseEntity<?> getFavoriteCount(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return data("count",0);
        }
        return data("count", userService.getFavoriteCount());
    }

    @DeleteMapping("/account")
    public ResponseEntity<?> deleteAccount(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "User not logged in"));
        }
        userService.hardDeleteUser();
        return success("Account permanently deleted");
    }
}