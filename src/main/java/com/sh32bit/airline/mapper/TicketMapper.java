package com.sh32bit.airline.mapper;

import com.sh32bit.airline.entity.Ticket;
import com.sh32bit.airline.response.TicketResponse;

public class TicketMapper {
    public static TicketResponse toResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .flightFrom(ticket.getFlight().getFrom().getName())
                .flightTo(ticket.getFlight().getTo().getName())
                .bookedAt(ticket.getBookedAt())
                .departureTime(ticket.getFlight().getDepartureTime())
                .arrivalTime(ticket.getFlight().getArrivalTime())
                .seatNumber(ticket.getSeatNumber())
                .status(ticket.getStatus())
                .price(ticket.getFlight().getPrice())
                .build();
    }
}
