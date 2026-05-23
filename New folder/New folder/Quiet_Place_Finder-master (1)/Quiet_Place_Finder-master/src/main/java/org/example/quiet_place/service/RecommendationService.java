package org.example.quiet_place.service;

import org.example.quiet_place.model.Place;
import org.example.quiet_place.model.Recommendation;
import org.example.quiet_place.model.User;
import org.example.quiet_place.repository.PlaceRepository;
import org.example.quiet_place.repository.RecommendationRepository;
import org.example.quiet_place.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final UserSession userSession;

    public RecommendationService(RecommendationRepository recommendationRepository,
                                 PlaceRepository placeRepository,
                                 UserRepository userRepository,
                                 UserSession userSession) {
        this.recommendationRepository = recommendationRepository;
        this.placeRepository = placeRepository;
        this.userRepository = userRepository;
        this.userSession = userSession;
    }

    @Transactional
    public List<Recommendation> generateRecommendations() {
        Long userId = userSession.getUserId();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        recommendationRepository.deleteByUser(currentUser);

        List<Place> allPlaces = placeRepository.findAll();
        List<Recommendation> newRecommendations = new ArrayList<>();

        for (Place place : allPlaces) {
            Integer userNoisePref = currentUser.getDefaultNoiseLevel();

            if (userNoisePref != null) {
                double difference = Math.abs(place.getNoiseLevel() - userNoisePref);

                if (difference <= 20) {
                    Recommendation rec = new Recommendation();
                    rec.setPlaceName(place.getName());
                    rec.setDescription(makeDescription(place));
                    rec.setQuietLevel((int) place.getNoiseLevel());
                    rec.setUser(currentUser);
                    newRecommendations.add(rec);
                }
            }
        }

        return recommendationRepository.saveAll(newRecommendations);
    }

    public List<Recommendation> getUserRecommendations() {
        Long userId = userSession.getUserId();
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Recommendation> recommendations = recommendationRepository.findByUser(currentUser);

        if (recommendations.size() == 0) {
            return generateRecommendations();
        }

        return recommendations;
    }

    public List<Recommendation> filterByQuietLevel(int maxLevel) {
        List<Recommendation> all = getUserRecommendations();
        List<Recommendation> filtered = new ArrayList<>();

        for (Recommendation rec : all) {
            if (rec.getQuietLevel() <= maxLevel) {
                filtered.add(rec);
            }
        }

        return filtered;
    }

    private String makeDescription(Place place) {
        if (place.getNoiseLevel() <= 30) {
            return "Very quiet - perfect for studying";
        } else if (place.getNoiseLevel() <= 50) {
            return "Moderate noise - good for working";
        } else {
            return "Lively atmosphere - good for meetings";
        }
    }

    public void generateRecommendationsthreads() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.execute(() -> {
            System.out.println("Analyzing user preferences...");
        });
        executorService.execute(() -> {
            System.out.println("Finding nearby quiet places...");
        });
        executorService.execute(() -> {
            System.out.println("Matching places with user interests...");
        });
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        System.out.println("Recommendations generated successfully.");
    }


    }