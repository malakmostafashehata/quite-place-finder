package org.example.quiet_place.repository;

import org.example.quiet_place.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

    @Repository
    public interface PlaceRepository extends JpaRepository<Place, Long> {

        //List<Place> findPlaceByPurposeIn(List<String> purpose);
        //List<Place> findPlaceByAmenitiesIn(List<String> amenities);
        List<Place> findPlaceByAddressContaining(String address);
        List<Place> findByAddressContaining(String address);
        List<Place> findByNameContaining(String name);

        List<Place> findPlaceByNameContaining(String name);
        Place findPlaceById(Long id);
        
    }
