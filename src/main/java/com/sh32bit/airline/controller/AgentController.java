package com.sh32bit.airline.controller;

import com.sh32bit.airline.request.FlightBatchUploadRequest;
import com.sh32bit.airline.request.FlightRequest;
import com.sh32bit.airline.request.FlightUpdateRequest;
import com.sh32bit.airline.response.FlightResponse;
import com.sh32bit.airline.response.MessageResponse;
import com.sh32bit.airline.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @PostMapping("/flights")
    public ResponseEntity<MessageResponse> createFlight(@RequestBody FlightRequest request) {
        return ResponseEntity.ok(agentService.createFlight(request));
    }

    @PostMapping("/flights/batch")
    public ResponseEntity<MessageResponse> batchCreateFlights(@RequestBody FlightBatchUploadRequest request) {
        return ResponseEntity.ok(agentService.batchCreateFlights(request));
    }

    @PostMapping("/flights/upload")
    public ResponseEntity<MessageResponse> uploadFlights(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(agentService.uploadFlights(file));
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<MessageResponse> updateFlight(@PathVariable Long id, @RequestBody FlightUpdateRequest request) {
        return ResponseEntity.ok(agentService.updateFlight(id, request));
    }

    @GetMapping("/flights/all")
    public ResponseEntity<List<FlightResponse>> getAgentFlights(@RequestParam(required = false) Long companyId) {
        return ResponseEntity.ok(agentService.getAgentFlights(companyId));
    }
}
