package org.example.quiet_place.repository;

import org.example.quiet_place.model.NoiseReading;
import org.example.quiet_place.model.Place;
import org.example.quiet_place.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoiseReadingRepository extends JpaRepository< NoiseReading, Long> {

    List<NoiseReading> findByPlace(Place place);

    List<NoiseReading> findTop10ByPlaceOrderByReadingTimeDesc(Place place);



    List<NoiseReading> findByUser(User user);


    int countByUser(User user);


    List<NoiseReading> findByIsVerifiedFalse();


    List<NoiseReading> findByIsVerifiedTrue();


    List<NoiseReading> findByPlaceAndIsVerifiedFalse(Place place);


    @Query("SELECT COUNT(n) FROM NoiseReading n WHERE n.user = :user AND DATE(n.readingTime) = CURRENT_DATE")
    int countUserReadingsToday(@Param("user") User user);



    default boolean canUserSubmitToday(User user) {
        return countUserReadingsToday(user) < 5;
    }



    default int getRemainingSubmissionsToday(User user) {
        return 5 - countUserReadingsToday(user); 
    }


    // ========== DATE RANGE QUERIES ==========


    // Find readings between two dates (for analytics)

    List<NoiseReading> findByReadingTimeBetween(LocalDateTime start, LocalDateTime end);




    List<NoiseReading> findByPlaceAndReadingTimeBetween(Place place, LocalDateTime start, LocalDateTime end);



    @Query("SELECT AVG(n.noiseLevel) FROM NoiseReading n WHERE n.place = :place AND n.isVerified = true")
    Double getAverageNoiseForPlace(@Param("place") Place place);


    @Query("SELECT AVG(n.crowdedness) FROM NoiseReading n WHERE n.place = :place AND n.isVerified = true")
    Double getAverageCrowdednessForPlace(@Param("place") Place place);



    @Query("SELECT COUNT(n) FROM NoiseReading n WHERE n.place = :place AND n.isVerified = true")
    Integer countVerifiedReadingsForPlace(@Param("place") Place place);


    @Modifying
    @Transactional
    @Query("UPDATE Place p SET " +
            "p.avgNoise = (SELECT COALESCE(AVG(n.noiseLevel), 0) FROM NoiseReading n WHERE n.place = p AND n.isVerified = true), " +
            "p.avgCrowdedness = (SELECT COALESCE(AVG(n.crowdedness), 0) FROM NoiseReading n WHERE n.place = p AND n.isVerified = true), " +
            "p.totalReadings = (SELECT COUNT(n) FROM NoiseReading n WHERE n.place = p AND n.isVerified = true) " +
            "WHERE p = :place")
    void updatePlaceAverages(@Param("place") Place place);


    void deleteByPlace(Place place);


    void deleteByReadingIdAndUser(Long readingId, User user);


    boolean existsByReadingIdAndUser(Long readingId, User user);


    @Modifying
    @Transactional
    @Query("UPDATE NoiseReading n SET n.isVerified = true WHERE n.readingId = :readingId")
    void verifyReading(@Param("readingId") Long readingId);


    @Modifying
    @Transactional
    void deleteByReadingId(Long readingId);

}
