package com.sh32bit.airline.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Airport from;

    @ManyToOne(optional = false)
    private Airport to;

    @ManyToOne(optional = false)
    private Company company;

    @ManyToOne(optional = false)
    private Agent agent;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column(nullable = false)
    private LocalDateTime arrivalTime;

    @Column(nullable = false)
    private int totalSeats;

    @Column(nullable = false)
    private int availableSeats;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "flight")
    private List<Ticket> tickets;
}
