package org.example.quiet_place.Controller;
import org.example.quiet_place.model.Place;
import org.example.quiet_place.service.PlaceService;
import org.example.quiet_place.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.example.quiet_place.model.Place;
import org.example.quiet_place.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/places")
@CrossOrigin(origins = "*")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    // Get all places
    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        List<Place> places = placeService.getAllPlaces();
        if (places.isEmpty()) {
            return ResponseEntity.noContent().build(); //  No Content return 204
        }
        return ResponseEntity.ok(places); //  OK return 200
    }

    // get places by id
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        Place place = placeService.getPlaceById(id);
        if (place == null) {
            return ResponseEntity.notFound().build(); // Not Found return 404
        }
        return ResponseEntity.ok(place);
    }

    // searching for nearby places using range
    @GetMapping("/nearby")
    public ResponseEntity<List<Place>> getNearbyPlaces(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "1000") double radius) {
        List<Place> places = placeService.findNearby(lat, lng, radius);
        if (places.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(places);
    }

    @GetMapping("/api/places/search")
    @ResponseBody
    public List<Place> searchPlaces(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radius,
            @RequestParam String noiseLevel,
            @RequestParam String purpose) {
        return placeService.findNearbythreads(lat, lng, radius, noiseLevel, purpose);
    }

    //filter places by max noise
    @GetMapping("/filter")
    public ResponseEntity<List<Place>> filterByNoise(@RequestParam(required = false) Integer maxNoise) {
        if (maxNoise == null) {

            return getAllPlaces();
        }
        List<Place> filtered = placeService.findByMaxNoise(maxNoise);
        if (filtered.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(filtered);
    }
}

