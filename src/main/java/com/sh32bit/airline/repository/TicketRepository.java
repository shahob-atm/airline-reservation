package com.sh32bit.airline.repository;

import com.sh32bit.airline.entity.Customer;
import com.sh32bit.airline.entity.Flight;
import com.sh32bit.airline.entity.Ticket;
import com.sh32bit.airline.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByCustomerId(Long customerId);
    boolean existsByCustomerAndFlightAndStatus(Customer customer, Flight flight, TicketStatus status);
}
