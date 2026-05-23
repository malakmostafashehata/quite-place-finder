package org.example.quiet_place.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/noise")
public class NoiseReadingController {

    @PostMapping("/submit")
    public ResponseEntity<?> submitNoise(@RequestParam long placeId) {
        return ResponseEntity.ok(Map.of("message","success"));
    }
}
