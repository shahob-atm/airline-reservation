package com.sh32bit.airline.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne
    private City city;

    @OneToMany(mappedBy = "from")
    private List<Flight> departures;

    @OneToMany(mappedBy = "to")
    private List<Flight> arrivals;
}
