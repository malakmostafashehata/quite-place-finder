package org.example.quiet_place.repository;

import org.example.quiet_place.model.Recommendation;
import org.example.quiet_place.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {


    List<Recommendation> findByUser(User user);


    void deleteByUser(User user);


}
