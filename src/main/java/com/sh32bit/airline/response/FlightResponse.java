package com.sh32bit.airline.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightResponse {
    private Long id;
    private String fromAirport;
    private String toAirport;
    private String companyName;
    private String agentFullName;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private int totalSeats;
    private int availableSeats;
    private BigDecimal price;
}
