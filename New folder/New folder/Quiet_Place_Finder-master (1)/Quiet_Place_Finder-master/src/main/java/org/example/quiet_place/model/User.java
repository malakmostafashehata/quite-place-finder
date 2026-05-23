package org.example.quiet_place.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users", indexes = {
        @Index(name = "id_email", columnList = "email"),
        @Index(name = "id_username", columnList = "username")
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "default_noise_level")
    private Integer defaultNoiseLevel;

    @Column(name = "default_range")
    private Double defaultRange;

    @Column(name = "use_current_location")
    private Boolean useCurrentLocation;

    @Column(name = "default_latitude")
    private Double defaultLatitude;

    @Column(name = "default_longitude")
    private Double defaultLongitude;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "user_favorite_places", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "place_id")
    private Set<Long> favoritePlaceIds = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getDefaultNoiseLevel() { return defaultNoiseLevel; }
    public void setDefaultNoiseLevel(Integer defaultNoiseLevel) { this.defaultNoiseLevel = defaultNoiseLevel; }

    public Double getDefaultRange() { return defaultRange; }
    public void setDefaultRange(Double defaultRange) { this.defaultRange = defaultRange; }

    public Boolean getUseCurrentLocation() { return useCurrentLocation; }
    public void setUseCurrentLocation(Boolean useCurrentLocation) { this.useCurrentLocation = useCurrentLocation; }

    public Double getDefaultLatitude() { return defaultLatitude; }
    public void setDefaultLatitude(Double defaultLatitude) { this.defaultLatitude = defaultLatitude; }

    public Double getDefaultLongitude() { return defaultLongitude; }
    public void setDefaultLongitude(Double defaultLongitude) { this.defaultLongitude = defaultLongitude; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public Set<Long> getFavoritePlaceIds() { return favoritePlaceIds; }
    public void setFavoritePlaceIds(Set<Long> favoritePlaceIds) { this.favoritePlaceIds = favoritePlaceIds; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    @ManyToMany
    @JoinTable(
            name = "favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    private Set<Place> favoritePlaces = new HashSet<>();
}
