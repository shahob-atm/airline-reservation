package com.sh32bit.airline.service.impl;

import com.sh32bit.airline.entity.*;
import com.sh32bit.airline.enums.TicketStatus;
import com.sh32bit.airline.exception.ConflictException;
import com.sh32bit.airline.exception.NotFoundException;
import com.sh32bit.airline.mapper.AirportMapper;
import com.sh32bit.airline.mapper.CustomerMapper;
import com.sh32bit.airline.mapper.FlightMapper;
import com.sh32bit.airline.mapper.TicketMapper;
import com.sh32bit.airline.repository.*;
import com.sh32bit.airline.response.*;
import com.sh32bit.airline.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final AirportRepository airportRepository;
    private final FlightRepository flightRepository;
    private final TicketRepository ticketRepository;

    @Override
    public CustomerResponse getProfile(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        return CustomerMapper.toCustomerResponse(customer);
    }

    @Override
    @Transactional
    public MessageResponse selectCity(Long customerId, Long cityId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new NotFoundException("City not found"));
        customer.setCity(city);
        customerRepository.save(customer);
        log.info("Customer {} selected city {}", customerId, city.getName());
        return new MessageResponse("City selected successfully");
    }

    @Override
    public List<AirportResponse> getAirportsByCustomerCity(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        if (customer.getCity() == null) {
            throw new ConflictException("City not selected");
        }
        List<Airport> airports = airportRepository.findAllByCityId(customer.getCity().getId());
        return airports.stream().map(AirportMapper::toResponse).toList();
    }

    @Override
    public List<FlightResponse> searchFlights(Long fromAirportId, Long toAirportId, LocalDate date) {
        if (date != null) {
            return flightRepository.findByDate(fromAirportId, toAirportId, date)
                    .stream().map(FlightMapper::fromEntity).toList();
        } else {
            return flightRepository.findWithoutDate(fromAirportId, toAirportId)
                    .stream().map(FlightMapper::fromEntity).toList();
        }
    }

    @Override
    @Transactional
    public MessageResponse buyTicket(Long customerId, Long flightId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NotFoundException("Flight not found"));
        if (flight.getAvailableSeats() <= 0) {
            throw new ConflictException("No available seats");
        }
        boolean alreadyBooked = ticketRepository.existsByCustomerAndFlightAndStatus(customer, flight, TicketStatus.BOOKED);
        if (alreadyBooked) {
            throw new ConflictException("Ticket already booked for this flight");
        }

        // Temp
        String seatNumber = "A" + (flight.getTotalSeats() - flight.getAvailableSeats() + 1);

        Ticket ticket = Ticket.builder()
                .customer(customer)
                .flight(flight)
                .seatNumber(seatNumber)
                .status(TicketStatus.BOOKED)
                .build();
        ticketRepository.save(ticket);

        flight.setAvailableSeats(flight.getAvailableSeats() - 1);
        flightRepository.save(flight);

        log.info("Customer {} bought ticket for flight {}", customerId, flightId);
        return new MessageResponse("Ticket bought successfully");
    }

    @Override
    @Transactional
    public MessageResponse cancelTicket(Long customerId, Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));
        if (!ticket.getCustomer().getId().equals(customerId)) {
            throw new ConflictException("You are not the owner of this ticket");
        }
        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new ConflictException("Ticket is already cancelled");
        }
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);

        Flight flight = ticket.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + 1);
        flightRepository.save(flight);

        log.info("Customer {} cancelled ticket {}", customerId, ticketId);
        return new MessageResponse("Ticket cancelled successfully");
    }

    @Override
    public List<TicketResponse> getTickets(Long customerId) {
        List<Ticket> tickets = ticketRepository.findAllByCustomerId(customerId);
        return tickets.stream().map(TicketMapper::toResponse).toList();
    }
}
