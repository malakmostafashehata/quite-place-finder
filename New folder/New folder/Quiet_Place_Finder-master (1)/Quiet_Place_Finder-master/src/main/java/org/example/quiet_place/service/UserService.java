package org.example.quiet_place.service;

import org.example.quiet_place.dto.LoginRequest;
import org.example.quiet_place.dto.RegisterRequest;
import org.example.quiet_place.model.User;
import org.example.quiet_place.repository.UserRepository;
import org.example.quiet_place.service.UserSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSession userSession;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserSession userSession) {
        this.userRepository = userRepository;
        this.userSession = userSession;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Long getCurrentUserId() {
        return userSession.getUserId();
    }

    public User getCurrentUser() {
        return userSession.getCurrentUser();
    }

    public boolean isLoggedIn() {
        return userSession.isLoggedIn();
    }

    public void logout() {
        userSession.logout();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(input)
                .orElseGet(() -> userRepository.findByUsername(input)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + input)));

        String role = "admin@gmail.com".equals(user.getEmail()) ? "ADMIN" : "USER";

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(role)
                .build();
    }

    private User getLoggedInUser() {
        Long userId = userSession.getUserId();
        if (userId == null) {
            throw new RuntimeException("Not logged in");
        }
        return userRepository.findById(userId).orElseThrow();
    }

    private void updateUser(Consumer<User> updater) {
        User user = getLoggedInUser();
        updater.accept(user);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        userSession.refreshActivity();
    }

    @Transactional
    public User register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        Optional<User> existingEmail = userRepository.findByEmail(request.getEmail());
        if (existingEmail.isPresent()) {
            throw new RuntimeException("Email already taken");
        }

        Optional<User> existingUsername = userRepository.findByUsername(request.getUsername());
        if (existingUsername.isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(request.getUsername(), request.getEmail(), encodedPassword);
        user.setDefaultNoiseLevel(50);
        user.setDefaultRange(5.0);
        user.setUseCurrentLocation(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public User login(LoginRequest request, HttpServletRequest httpRequest) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByUsername(request.getEmail());
        }

        User user = userOpt.orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        userSession.login(user);

        HttpSession session = httpRequest.getSession();

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }

    @Transactional
    public void updateNoisePreference(Integer noiseLevel) {
        if (noiseLevel < 0 || noiseLevel > 100) {
            throw new RuntimeException("Noise level must be 0-100");
        }
        updateUser(user -> user.setDefaultNoiseLevel(noiseLevel));
    }

    @Transactional
    public void updateDefaultRange(Double range) {
        if (range <= 0 || range > 50) {
            throw new RuntimeException("Range must be 0.1-50 km");
        }
        updateUser(user -> user.setDefaultRange(range));
    }

    @Transactional
    public void updateLocation(Double lat, Double lng) {
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new RuntimeException("Invalid coordinates");
        }
        updateUser(user -> {
            user.setDefaultLatitude(lat);
            user.setDefaultLongitude(lng);
        });
    }

    @Transactional
    public void toggleAutoLocation(Boolean useAuto) {
        updateUser(user -> user.setUseCurrentLocation(useAuto));
    }

    @Transactional
    public void addFavorite(Long userId, Long placeId) {
        User user = getLoggedInUser();
        Set<Long> favorites = userRepository.getFavoritePlaceIds(user.getId());
        if (favorites.contains(placeId)) {
            throw new RuntimeException("Place already in favorites");
        }
        userRepository.addFavorite(userId, placeId);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        userSession.refreshActivity();
    }

    @Transactional
    public  void removeFavorite(Long placeId, Long userId) {
        User user = getLoggedInUser();
        userRepository.removeFavorite(userId, placeId);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        userSession.refreshActivity();
    }

    public Set<Long> getUserFavorites() {
        User user = getLoggedInUser();
        userSession.refreshActivity();
        return userRepository.getFavoritePlaceIds(user.getId());
    }

    public boolean isPlaceFavorite(Long placeId) {
        Long userId = userSession.getUserId();
        if (userId == null) {
            return false;
        }
        userSession.refreshActivity();
        Set<Long> favorites = userRepository.getFavoritePlaceIds(userId);
        return favorites.contains(placeId);
    }

    public int getFavoriteCount() {
        Long userId = userSession.getUserId();
        if (userId == null) {
            return 0;
        }
        userSession.refreshActivity();
        return userRepository.countFavorites(userId);
    }

    @Transactional
    public void hardDeleteUser() {
        User user = getLoggedInUser();
        userRepository.hardDeleteUserById(user.getId());
        logout();
    }
}
