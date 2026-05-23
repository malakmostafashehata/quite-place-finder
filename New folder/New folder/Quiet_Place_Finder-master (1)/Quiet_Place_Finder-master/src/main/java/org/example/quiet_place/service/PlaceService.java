package org.example.quiet_place.service;

import org.example.quiet_place.model.Place;
import org.example.quiet_place.repository.PlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Transactional
    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }

    @Transactional
    public Place updatePlace(Long id, Place place) {

        Place existingPlace = placeRepository.findPlaceById(id);
        if (existingPlace == null) {
            throw new RuntimeException("place with id " + id + " not found in system");
        }
        existingPlace.setAddress(place.getAddress());
        existingPlace.setAmenities(place.getAmenities());
        existingPlace.setName(place.getName());
        existingPlace.setNoiseLevel(place.getNoiseLevel());
        existingPlace.setPurpose(place.getPurpose());
        existingPlace.setLatitude(place.getLatitude());
        existingPlace.setLongitude(place.getLongitude());
        return placeRepository.save(existingPlace);


    }

    @Transactional
    public Place addPlace(Place place) {

        if (place.getName() == null || place.getName().isBlank()) {
            throw new RuntimeException("Place name cannot be empty");
        }
        if (place.getAddress() == null || place.getAddress().isBlank()) {
            throw new RuntimeException("place must have an address");
        }
        if (place.getAmenities() == null || place.getAmenities().isEmpty()) {
            throw new RuntimeException("Place cannot have no Amenities");
        }
        if (place.getPurpose() == null || place.getPurpose().isEmpty()) {
            throw new RuntimeException("Purpose of place must be specified");
        }

        if (place.getLongitude() == 0.0 && place.getLatitude() == 0.0) {
            throw new RuntimeException("place must have coordinates");
        }
        return placeRepository.save(place);
    }


    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }


    public Place getPlaceById(Long id) {
        return placeRepository.findPlaceById(id);
    }

    //this is the haversine formula. might change it to a simpler formula that assumes a flat surface instead later on.
    private double calculateDistance(double userLat, double userLng, double placeLat, double placeLng) {
        final double EARTH_RADIUS_M = 6_371_000;

        double lat1 = Math.toRadians(userLat);
        double lng1 = Math.toRadians(userLng);
        double lat2 = Math.toRadians(placeLat);
        double lng2 = Math.toRadians(placeLng);

        double dLat = lat2 - lat1;
        double dLng = lng2 - lng1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_M * c;
    }

    public List<Place> findByMaxNoise(int maxNoise) {
        List<Place> allPlaces = placeRepository.findAll();
        List<Place> filtered = new ArrayList<>();

        for (Place place : allPlaces) {
            if (place.getNoiseLevel() <= maxNoise) {
                filtered.add(place);
            }
        }
        return filtered;
    }
    public List<Place> findNearby(double userLat, double userLng, double radius) {
        List<Place> allPlaces = placeRepository.findAll();
        List<Place> nearbyPlaces = new ArrayList<>();

        for (Place place : allPlaces) {
            double distance = calculateDistance(userLat, userLng,
                    place.getLatitude(), place.getLongitude());
            if (distance <= radius) {
                nearbyPlaces.add(place);
            }
        }
        return nearbyPlaces;
    }

    public List<Place> findNearbythreads(double userLat, double userLng, double radius, String noiseLevel, String purpose) {

        List<Place> allPlaces = placeRepository.findAll();
        List<Place> nearbyPlaces = new CopyOnWriteArrayList<>();

        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (Place place : allPlaces) {

            executor.execute(() -> {

                double distance = calculateDistance(
                        userLat,
                        userLng,
                        place.getLatitude(),
                        place.getLongitude()
                );

                if (distance <= radius) {
                    nearbyPlaces.add(place);
                }
            });
        }
        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return nearbyPlaces;
    }
    public List<Place> getPlacesByIds(Set<Long> ids) {
        return placeRepository.findAllById(ids);
    }
}