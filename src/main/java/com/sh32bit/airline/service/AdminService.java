package com.sh32bit.airline.service;

import com.sh32bit.airline.request.AgentRequest;
import com.sh32bit.airline.request.AirportRequest;
import com.sh32bit.airline.request.CityRequest;
import com.sh32bit.airline.request.CompanyRequest;
import com.sh32bit.airline.response.MessageResponse;
import jakarta.validation.Valid;

public interface AdminService {
    MessageResponse createAgent(@Valid AgentRequest request);

    MessageResponse deleteAgent(Long id);

    MessageResponse createCity(@Valid CityRequest request);

    MessageResponse createAirport(@Valid AirportRequest request);

    MessageResponse createCompany(@Valid CompanyRequest request);
}
