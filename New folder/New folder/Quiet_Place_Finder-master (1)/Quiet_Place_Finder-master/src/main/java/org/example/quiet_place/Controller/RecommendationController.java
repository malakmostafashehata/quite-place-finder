package org.example.quiet_place.Controller;

import org.example.quiet_place.model.Recommendation;
import org.example.quiet_place.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }


    @GetMapping
    public ResponseEntity<List<Recommendation>> getRecommendations() {
        return ResponseEntity.ok(recommendationService.getUserRecommendations());
    }


    @PostMapping("/refresh")
    public ResponseEntity<List<Recommendation>> refreshRecommendations() {
        return ResponseEntity.ok(recommendationService.generateRecommendations());
    }
    @GetMapping("/filter")
    public ResponseEntity<List<Recommendation>> filterRecommendations(@RequestParam int maxLevel) {
        return ResponseEntity.ok(recommendationService.filterByQuietLevel(maxLevel));
    }
}
