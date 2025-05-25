package com.sh32bit.airline.service;

import com.sh32bit.airline.response.*;

import java.time.LocalDate;
import java.util.List;

public interface CustomerService {
    CustomerResponse getProfile(Long customerId);
    MessageResponse selectCity(Long customerId, Long cityId);
    List<AirportResponse> getAirportsByCustomerCity(Long customerId);
    List<FlightResponse> searchFlights(Long fromAirportId, Long toAirportId, LocalDate date);
    MessageResponse buyTicket(Long customerId, Long flightId);
    MessageResponse cancelTicket(Long customerId, Long ticketId);
    List<TicketResponse> getTickets(Long customerId);
}
