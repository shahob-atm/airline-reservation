package com.sh32bit.airline.service;

import com.sh32bit.airline.request.FlightBatchUploadRequest;
import com.sh32bit.airline.request.FlightRequest;
import com.sh32bit.airline.request.FlightUpdateRequest;
import com.sh32bit.airline.response.FlightResponse;
import com.sh32bit.airline.response.MessageResponse;
import com.sh32bit.airline.security.AppUserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AgentService {
    MessageResponse createFlight(FlightRequest request, AppUserDetails user);

    MessageResponse batchCreateFlights(FlightBatchUploadRequest request, AppUserDetails user);

    MessageResponse uploadFlights(MultipartFile file, AppUserDetails user);

    MessageResponse updateFlight(Long id, FlightUpdateRequest request, AppUserDetails user);

    List<FlightResponse> getAgentFlights(Long companyId, AppUserDetails user);
}
