package com.sh32bit.airline.controller;

import com.sh32bit.airline.request.CitySelectRequest;
import com.sh32bit.airline.request.TicketRequest;
import com.sh32bit.airline.response.*;
import com.sh32bit.airline.security.AppUserDetails;
import com.sh32bit.airline.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/profile")
    public ResponseEntity<CustomerResponse> getProfile(@AuthenticationPrincipal AppUserDetails user) {
        return ResponseEntity.ok(customerService.getProfile(user.getUser().getId()));
    }

    @PutMapping("/profile/city")
    public ResponseEntity<MessageResponse> selectCity(@AuthenticationPrincipal AppUserDetails user,
                                                      @RequestBody @Valid CitySelectRequest request) {
        return ResponseEntity.ok(customerService.selectCity(user.getUser().getId(), request.getCityId()));
    }

    @GetMapping("/airports")
    public ResponseEntity<List<AirportResponse>> getAirports(@AuthenticationPrincipal AppUserDetails user) {
        return ResponseEntity.ok(customerService.getAirportsByCustomerCity(user.getUser().getId()));
    }

    @GetMapping("/flights")
    public ResponseEntity<List<FlightResponse>> searchFlights(
            @RequestParam(required = false) Long fromAirportId,
            @RequestParam(required = false) Long toAirportId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ResponseEntity.ok(customerService.searchFlights(fromAirportId, toAirportId, date));
    }

    @PostMapping("/tickets")
    public ResponseEntity<MessageResponse> buyTicket(@AuthenticationPrincipal AppUserDetails user,
                                                     @RequestBody @Valid TicketRequest request) {
        return ResponseEntity.ok(customerService.buyTicket(user.getUser().getId(), request.getFlightId()));
    }

    @DeleteMapping("/tickets/{ticketId}")
    public ResponseEntity<MessageResponse> cancelTicket(@AuthenticationPrincipal AppUserDetails user,
                                                        @PathVariable Long ticketId) {
        return ResponseEntity.ok(customerService.cancelTicket(user.getUser().getId(), ticketId));
    }

    @GetMapping("/tickets/all")
    public ResponseEntity<List<TicketResponse>> getTickets(@AuthenticationPrincipal AppUserDetails user) {
        return ResponseEntity.ok(customerService.getTickets(user.getUser().getId()));
    }
}
