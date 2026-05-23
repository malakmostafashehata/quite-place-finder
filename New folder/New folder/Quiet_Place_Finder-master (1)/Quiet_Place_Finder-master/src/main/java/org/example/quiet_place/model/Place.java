package org.example.quiet_place.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("unused")
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double noiseLevel;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private Double avgNoise;
    private Double avgCrowdedness;
    private Integer totalReadings;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> purpose = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> amenities = new ArrayList<>();


    public Place(){}

    public Place(double noiseLevel, long id, String name, String address,double latitude, double longitude, List<String> purpose, List<String> amenities) {
        this.noiseLevel = noiseLevel;
        this.id = id;
        this.name = name;
        this.latitude=latitude;
        this.longitude=longitude;
        this.address = address;
        this.purpose = purpose;
        this.amenities = amenities;
    }

    public Double getAvgNoise() {return avgNoise;}
    public void setAvgNoise(Double avgNoise) {this.avgNoise = avgNoise;}

    public Double getAvgCrowdedness() {return avgCrowdedness;}
    public void setAvgCrowdedness(Double avgCrowdedness) {this.avgCrowdedness = avgCrowdedness;}

    public Integer getTotalReadings() {return totalReadings;}
    public void setTotalReadings(Integer totalReadings) {this.totalReadings = totalReadings;}

    public Long getId() {
        return id;
    }

    public double getNoiseLevel() {
        return noiseLevel;
    }
    public void setNoiseLevel(double noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getPurpose() {
        return purpose;
    }
    public void setPurpose(List<String> purpose) {
        this.purpose = purpose;
    }

    public List<String> getAmenities() {
        return amenities;
    }
    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public void setId(Long id) {this.id = id;}

    public double getLatitude() {return latitude;}

    public void setLatitude(double latitude) {this.latitude = latitude;}

    public double getLongitude() {return longitude;}
    public void setLongitude(double longitude) {this.longitude = longitude;}
}
