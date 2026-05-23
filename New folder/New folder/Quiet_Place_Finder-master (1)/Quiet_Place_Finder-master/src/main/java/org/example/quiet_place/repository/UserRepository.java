package org.example.quiet_place.repository;

import org.example.quiet_place.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favoritePlaceIds WHERE u.id = :userId")
    Optional<User> findByIdWithFavorites(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.lastLogin = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.defaultNoiseLevel = :noiseLevel WHERE u.id = :userId")
    void updateNoisePreference(@Param("userId") Long userId, @Param("noiseLevel") Integer noiseLevel);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void hardDeleteUserById(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_favorite_places (user_id, place_id) VALUES (:userId, :placeId)", nativeQuery = true)
    void addFavorite(@Param("userId") Long userId, @Param("placeId") Long placeId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user_favorite_places WHERE user_id = :userId AND place_id = :placeId", nativeQuery = true)
    void removeFavorite(@Param("userId") Long userId, @Param("placeId") Long placeId);

    @Query("SELECT u.favoritePlaceIds FROM User u WHERE u.id = :userId")
    Set<Long> getFavoritePlaceIds(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM User u JOIN u.favoritePlaceIds p WHERE u.id = :userId")
    int countFavorites(@Param("userId") Long userId);
}
