package com.sh32bit.airline.controller;

import com.sh32bit.airline.request.AgentRequest;
import com.sh32bit.airline.request.AirportRequest;
import com.sh32bit.airline.request.CityRequest;
import com.sh32bit.airline.request.CompanyRequest;
import com.sh32bit.airline.response.AgentResponse;
import com.sh32bit.airline.response.CustomerResponse;
import com.sh32bit.airline.response.MessageResponse;
import com.sh32bit.airline.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/cities")
    public ResponseEntity<MessageResponse> createCity(@RequestBody @Valid CityRequest request) {
        return ResponseEntity.ok(adminService.createCity(request));
    }

    @PostMapping("/airports")
    public ResponseEntity<MessageResponse> createAirport(@RequestBody @Valid AirportRequest request) {
        return ResponseEntity.ok(adminService.createAirport(request));
    }

    @PostMapping("/companies")
    public ResponseEntity<MessageResponse> createCompany(@RequestBody @Valid CompanyRequest request) {
        return ResponseEntity.ok(adminService.createCompany(request));
    }

    @PostMapping("/agents")
    public ResponseEntity<MessageResponse> createAgent(@RequestBody @Valid AgentRequest request) {
        return ResponseEntity.ok(adminService.createAgent(request));
    }

    @PutMapping("/customers/{id}/block")
    public ResponseEntity<MessageResponse> blockCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.blockCustomer(id));
    }

    @PutMapping("/customers/{id}/unblock")
    public ResponseEntity<MessageResponse> unblockCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.unblockCustomer(id));
    }

    @PutMapping("/agents/{id}/block")
    public ResponseEntity<MessageResponse> blockAgent(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.blockAgent(id));
    }

    @PutMapping("/agents/{id}/unblock")
    public ResponseEntity<MessageResponse> unblockAgent(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.unblockAgent(id));
    }

    @GetMapping("/agents/all")
    public ResponseEntity<List<AgentResponse>> getAllAgents() {
        return ResponseEntity.ok(adminService.getAllAgents());
    }

    @GetMapping("/customers/all")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(adminService.getAllCustomers());
    }
}
