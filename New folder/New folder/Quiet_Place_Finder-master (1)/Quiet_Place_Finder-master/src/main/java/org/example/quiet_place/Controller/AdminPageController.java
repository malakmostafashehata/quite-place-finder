package org.example.quiet_place.Controller;
import org.example.quiet_place.model.Place;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
import org.example.quiet_place.service.PlaceService;
import org.example.quiet_place.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;

@Controller
@RequestMapping("/admin")

public class AdminPageController {
    private final UserService userService;
    private final PlaceService placeService;

    public  AdminPageController(UserService userService, PlaceService placeService) {
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalPlaces", placeService.getAllPlaces().size());
        model.addAttribute("totalFavorites", 0);
        model.addAttribute("recentUsers", userService.getAllUsers().stream().limit(5).toList());
        return "admin/dashboard";
    }
    @GetMapping("/manage-users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/manage-users";
    }

    @GetMapping("/manage-places")
    public String managePlaces(Model model) {
        model.addAttribute("places", placeService.getAllPlaces());
        return "admin/manage-places";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        return "redirect:/admin/manage-users";
    }

    @PostMapping("/places")
    public String addPlace(@RequestParam String name,
                           @RequestParam String address,
                           @RequestParam Double noiseLevel,
                           @RequestParam Double latitude,
                           @RequestParam Double longitude,
                           @RequestParam(required = false) String purpose,
                           @RequestParam(required = false) String amenities,
                           RedirectAttributes redirectAttributes) {
        try {
            Place place = new Place();
            place.setName(name);
            place.setAddress(address);
            place.setNoiseLevel(noiseLevel);
            place.setLatitude(latitude);
            place.setLongitude(longitude);

            if (purpose != null && !purpose.isEmpty()) {
                place.setPurpose(Arrays.asList(purpose.split(",")));
            }
            if (amenities != null && !amenities.isEmpty()) {
                place.setAmenities(Arrays.asList(amenities.split(",")));
            }

            placeService.addPlace(place);
            redirectAttributes.addFlashAttribute("message", "Place added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/manage-places";
    }

    @PostMapping("/places/{id}/delete")
    public String deletePlace(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            placeService.deletePlace(id);
            redirectAttributes.addFlashAttribute("message", "Place deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/manage-places";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
