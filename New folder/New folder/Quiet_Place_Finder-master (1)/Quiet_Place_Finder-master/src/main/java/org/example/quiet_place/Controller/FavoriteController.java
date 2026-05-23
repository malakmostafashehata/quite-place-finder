package org.example.quiet_place.Controller;

import org.example.quiet_place.model.Place;
import org.example.quiet_place.service.PlaceService;
import org.example.quiet_place.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import jakarta.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final UserService userService;
    private final PlaceService placeService;

    public FavoriteController(UserService userService, PlaceService placeService) {
        this.userService = userService;
        this.placeService = placeService;
    }

    @GetMapping
    public String getFavoritesPage(HttpSession session, Model model) {
        // Get logged-in user
        Object userId = session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login";
        }

        Set<Long> favoriteIds = userService.getUserFavorites();
        List<Place> favoritePlaces = placeService.getPlacesByIds(favoriteIds);
        model.addAttribute("favorites", favoritePlaces);
        return "favorites";
    }


    @PostMapping("/add/{placeId}")
    public String addFavorite(@PathVariable Long placeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            userService.addFavorite(userId, placeId);
        }

        return "redirect:/favorites";
    }

    @PostMapping("/remove/{placeId}")
    public String removeFavorite(@PathVariable Long placeId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId != null) {
            userService.removeFavorite(userId, placeId);
        }

        return "redirect:/favorites";
    }
}