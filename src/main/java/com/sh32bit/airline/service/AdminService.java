package com.sh32bit.airline.service;

import com.sh32bit.airline.request.AgentRequest;
import com.sh32bit.airline.request.AirportRequest;
import com.sh32bit.airline.request.CityRequest;
import com.sh32bit.airline.request.CompanyRequest;
import com.sh32bit.airline.response.AgentResponse;
import com.sh32bit.airline.response.CustomerResponse;
import com.sh32bit.airline.response.MessageResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface AdminService {
    MessageResponse createCity(@Valid CityRequest request);

    MessageResponse createAirport(@Valid AirportRequest request);

    MessageResponse createCompany(@Valid CompanyRequest request);

    MessageResponse createAgent(@Valid AgentRequest request);

    MessageResponse blockCustomer(Long id);

    MessageResponse unblockCustomer(Long id);

    MessageResponse blockAgent(Long id);

    MessageResponse unblockAgent(Long id);

    List<AgentResponse> getAllAgents();

    List<CustomerResponse> getAllCustomers();
}
