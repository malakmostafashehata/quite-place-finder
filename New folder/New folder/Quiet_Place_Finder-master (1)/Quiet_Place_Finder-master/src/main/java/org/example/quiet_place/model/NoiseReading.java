package org.example.quiet_place.model;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import java.time.LocalDateTime;

@Entity
public class NoiseReading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long readingId;
   // private long placeId;
    //private long userId;
    private int noiseLevel;
    private int crowdedness;
    private boolean isVerified;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;
    private LocalDateTime readingTime;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public NoiseReading(LocalDateTime readingTime, int crowdedness, int noiseLevel,
                        long userId, long placeId, long readingId) {
        this.readingTime = readingTime;
        this.crowdedness = crowdedness;
        this.noiseLevel = noiseLevel;
       // this.userId = userId;
        //this.placeId = placeId;
        this.readingId = readingId;
    }

    public NoiseReading() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getReadingId() {
        return readingId;
    }

    public void setReadingId(long readingId) {
        this.readingId = readingId;
    }

   /* public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }*/

    public int getNoiseLevel() {
        return noiseLevel;
    }

    public void setNoiseLevel(int noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public int getCrowdedness() {
        return crowdedness;
    }

    public void setCrowdedness(int crowdedness) {
        this.crowdedness = crowdedness;
    }

    public LocalDateTime getReadingTime() {
        return readingTime;
    }

    public void setReadingTime(LocalDateTime readingTime) {
        this.readingTime = readingTime;
    }
}

