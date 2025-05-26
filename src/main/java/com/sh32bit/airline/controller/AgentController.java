package com.sh32bit.airline.controller;

import com.sh32bit.airline.request.FlightBatchUploadRequest;
import com.sh32bit.airline.request.FlightRequest;
import com.sh32bit.airline.request.FlightUpdateRequest;
import com.sh32bit.airline.response.FlightResponse;
import com.sh32bit.airline.response.MessageResponse;
import com.sh32bit.airline.security.AppUserDetails;
import com.sh32bit.airline.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {
    private final AgentService agentService;

    @PostMapping("/flights")
    public ResponseEntity<MessageResponse> createFlight(@AuthenticationPrincipal AppUserDetails user,
                                                        @RequestBody FlightRequest request) {
        return ResponseEntity.ok(agentService.createFlight(request, user));
    }

    @PostMapping("/flights/batch")
    public ResponseEntity<MessageResponse> batchCreateFlights(@AuthenticationPrincipal AppUserDetails user,
                                                              @RequestBody FlightBatchUploadRequest request) {
        return ResponseEntity.ok(agentService.batchCreateFlights(request, user));
    }

    @PostMapping("/flights/upload")
    public ResponseEntity<MessageResponse> uploadFlights(@AuthenticationPrincipal AppUserDetails user,
                                                         @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(agentService.uploadFlights(file, user));
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<MessageResponse> updateFlight(@AuthenticationPrincipal AppUserDetails user,
                                                        @PathVariable Long id,
                                                        @RequestBody FlightUpdateRequest request) {
        return ResponseEntity.ok(agentService.updateFlight(id, request, user));
    }

    @GetMapping("/flights/all")
    public ResponseEntity<List<FlightResponse>> getAgentFlights(@AuthenticationPrincipal AppUserDetails user,
                                                                @RequestParam(required = false) Long companyId) {
        return ResponseEntity.ok(agentService.getAgentFlights(companyId, user));
    }
}
