package com.sh32bit.airline.response;

import com.sh32bit.airline.enums.TicketStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketResponse {
    private Long id;
    private String flightFrom;
    private String flightTo;
    private LocalDateTime bookedAt;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String seatNumber;
    private TicketStatus status;
    private BigDecimal price;
}
