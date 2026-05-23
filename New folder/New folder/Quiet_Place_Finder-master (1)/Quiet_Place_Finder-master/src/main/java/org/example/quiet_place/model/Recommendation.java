package org.example.quiet_place.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String placeName;
    private String description;
    private int quietLevel;

    @ManyToOne
    @JoinColumn(name = "user id")
    private User user;

}
