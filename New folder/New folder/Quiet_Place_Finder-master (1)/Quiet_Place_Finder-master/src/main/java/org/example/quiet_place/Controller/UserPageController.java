package org.example.quiet_place.Controller;
import org.example.quiet_place.dto.LoginRequest;
import org.example.quiet_place.dto.RegisterRequest;
import org.example.quiet_place.model.Place;
import org.example.quiet_place.model.User;
import org.example.quiet_place.service.PlaceService;
import org.example.quiet_place.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserPageController {

    private final UserService userService;
    private final PlaceService placeService;



    public UserPageController(UserService userService, PlaceService placeService) {
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam String role,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {
        try {
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setEmail(email);
            loginRequest.setPassword(password);

            User user = userService.login(loginRequest, request);

            if ("admin".equals(role) && !"admin@gmail.com".equals(email)) {
                redirectAttributes.addFlashAttribute("error", "wrong admin email or password");
                return "redirect:/login";
            }

            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("role", role);
            request.getSession().setAttribute("userId", user.getId());
            request.getSession().setAttribute("email", user.getEmail());

            if ("admin".equals(role)) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/home";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
        try {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername(username);
            registerRequest.setEmail(email);
            registerRequest.setPassword(password);
            registerRequest.setConfirmPassword(confirmPassword);

            User user = userService.register(registerRequest);

            // Auto-login after registration
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("userId", user.getId());
            request.getSession().setAttribute("role", "user");
            request.getSession().setAttribute("email", user.getEmail());

            redirectAttributes.addFlashAttribute("success", "Registration successful!");
            return "redirect:/home";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/home/search")
    public String homeSearch(@RequestParam String location,
                             @RequestParam double radius,
                             @RequestParam(defaultValue = "any") String noiseLevel,
                             @RequestParam(defaultValue = "any") String purpose,
                             @RequestParam(defaultValue = "any") String amenities,
                             Model model) {
        // TODO: Implement search logic
        List<Place> searchResults = placeService.getAllPlaces(); // placeholder
        model.addAttribute("places", searchResults);
        return "home";
    }

    @GetMapping("/explore")
    public String explore(@RequestParam(required = false) String search, Model model) {
        List<Place> places = placeService.getAllPlaces();

        // Filter by search term if provided
        if (search != null && !search.isEmpty()) {
            places = places.stream()
                    .filter(p -> p.getName().toLowerCase().contains(search.toLowerCase()) ||
                            p.getAddress().toLowerCase().contains(search.toLowerCase()))
                    .collect(Collectors.toList());
        }

        model.addAttribute("places", places);
        return "explore";
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");

        System.out.println("Profile accessed - User from session: " + user);

        if (user == null) {
            System.out.println("User is null, redirecting to login");
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/delete-account")
    public String deleteAccount(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            userService.deleteUser(user.getId());
            request.getSession().invalidate();
        }

        return "redirect:/login";

    }

    @GetMapping("/place/{id}")
    public String getPlaceDetails(@PathVariable Long id, Model model) {
        Place place = placeService.getPlaceById(id);
        if (place == null) {
            return "redirect:/explore";
        }
        model.addAttribute("place", place);
        return "place-details";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {

        request.getSession().invalidate();

        return "redirect:/login";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
}