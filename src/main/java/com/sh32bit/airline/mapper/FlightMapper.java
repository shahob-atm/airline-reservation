package com.sh32bit.airline.mapper;

import com.sh32bit.airline.entity.Flight;
import com.sh32bit.airline.request.FlightRequest;
import com.sh32bit.airline.response.FlightResponse;

public class FlightMapper {
    public static FlightResponse fromEntity(Flight flight) {
        return new FlightResponse(
                flight.getId(),
                flight.getFrom().getName(),
                flight.getTo().getName(),
                flight.getCompany().getName(),
                flight.getAgent().getFirstName() + " " + flight.getAgent().getLastName(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                flight.getTotalSeats(),
                flight.getAvailableSeats(),
                flight.getPrice()
        );
    }

    public static Flight toEntity(FlightRequest request) {
        return Flight.builder()
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .totalSeats(request.getTotalSeats())
                .availableSeats(request.getTotalSeats())
                .price(request.getPrice())
                .build();
    }
}
