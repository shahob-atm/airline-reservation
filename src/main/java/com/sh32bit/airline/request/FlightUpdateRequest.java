package com.sh32bit.airline.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightUpdateRequest {
    @Future(message = "Departure time must be in the future")
    private LocalDateTime departureTime;

    @Future(message = "Arrival time must be in the future")
    private LocalDateTime arrivalTime;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private BigDecimal price;

    private Integer totalSeats;
}
