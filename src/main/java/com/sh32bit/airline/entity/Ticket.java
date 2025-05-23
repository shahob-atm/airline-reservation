package com.sh32bit.airline.entity;

import com.sh32bit.airline.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime bookedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String seatNumber;

    @ManyToOne
    private Flight flight;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    @PrePersist
    public void onCreate() {
        bookedAt = LocalDateTime.now();
    }
}
