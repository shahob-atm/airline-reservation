package com.sh32bit.airline.service;

import com.sh32bit.airline.request.FlightBatchUploadRequest;
import com.sh32bit.airline.request.FlightRequest;
import com.sh32bit.airline.request.FlightUpdateRequest;
import com.sh32bit.airline.response.FlightResponse;
import com.sh32bit.airline.response.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AgentService {
    MessageResponse createFlight(FlightRequest request);

    MessageResponse batchCreateFlights(FlightBatchUploadRequest request);

    MessageResponse uploadFlights(MultipartFile file);

    MessageResponse updateFlight(Long id, FlightUpdateRequest request);

    List<FlightResponse> getAgentFlights(Long companyId);
}
