package com.sh32bit.airline.controller;

import com.sh32bit.airline.request.AgentRequest;
import com.sh32bit.airline.request.AirportRequest;
import com.sh32bit.airline.request.CityRequest;
import com.sh32bit.airline.request.CompanyRequest;
import com.sh32bit.airline.response.MessageResponse;
import com.sh32bit.airline.service.AdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Admin APIs")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/agents")
    public ResponseEntity<MessageResponse> createAgent(@RequestBody @Valid AgentRequest request) {
        return ResponseEntity.ok(adminService.createAgent(request));
    }

    @PostMapping("/cities")
    public ResponseEntity<MessageResponse> createCity(@Valid @RequestBody CityRequest request) {
        return ResponseEntity.ok(adminService.createCity(request));
    }

    @PostMapping("/airports")
    public ResponseEntity<MessageResponse> createAirport(@Valid @RequestBody AirportRequest request) {
        return ResponseEntity.ok(adminService.createAirport(request));
    }

    @PostMapping("/companies")
    public ResponseEntity<MessageResponse> createCompany(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(adminService.createCompany(request));
    }

    @DeleteMapping("/agents/{id}")
    public ResponseEntity<MessageResponse> deleteAgent(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteAgent(id));
    }
}
