package com.sh32bit.airline.service.impl;

import com.sh32bit.airline.entity.Agent;
import com.sh32bit.airline.entity.Airport;
import com.sh32bit.airline.entity.City;
import com.sh32bit.airline.entity.Company;
import com.sh32bit.airline.enums.Role;
import com.sh32bit.airline.repository.AgentRepository;
import com.sh32bit.airline.repository.AirportRepository;
import com.sh32bit.airline.repository.CityRepository;
import com.sh32bit.airline.repository.CompanyRepository;
import com.sh32bit.airline.request.AgentRequest;
import com.sh32bit.airline.request.AirportRequest;
import com.sh32bit.airline.request.CityRequest;
import com.sh32bit.airline.request.CompanyRequest;
import com.sh32bit.airline.response.MessageResponse;
import com.sh32bit.airline.service.AdminService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AgentRepository agentRepository;
    private final CompanyRepository companyRepository;
    private final AirportRepository airportRepository;
    private final PasswordEncoder passwordEncoder;
    private final CityRepository cityRepository;

    @Override
    public MessageResponse createAgent(AgentRequest request) {
        if (agentRepository.existsByCompanyIdAndAirportId(request.getCompanyId(), request.getAirportId())) {
            log.warn("Agent already exists for companyId={} and airportId={}",
                    request.getCompanyId(), request.getAirportId());
            throw new IllegalArgumentException("It is already an agent for the company and the airport");
        }

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        log.info("company exists: {}", company);

        Airport airport = airportRepository.findById(request.getAirportId())
                .orElseThrow(() -> new EntityNotFoundException("Airport not found"));
        log.info("airport exists: {}", airport);

        Agent agent = Agent.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .active(true)
                .role(Role.AGENT)
                .company(company)
                .airport(airport)
                .build();

        log.info("Agent created for companyId={} and airportId={}", request.getCompanyId(), request.getAirportId());
        agentRepository.save(agent);
        return new MessageResponse("Agent successfully created");
    }

    @Override
    public MessageResponse createCity(CityRequest request) {
        City city = City.builder()
                .name(request.getName())
                .build();

        cityRepository.save(city);
        log.info("Creating city={}", city);
        return new MessageResponse("City created successfully");
    }

    @Override
    public MessageResponse createAirport(AirportRequest request) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new EntityNotFoundException("City not found"));
        log.info("City exists: {}", city);

        Airport airport = Airport.builder()
                .name(request.getName())
                .city(city)
                .build();

        airportRepository.save(airport);
        log.info("Airport created: {}", airport);
        return new MessageResponse("Airport created successfully");
    }

    @Override
    public MessageResponse createCompany(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.getName())
                .build();

        companyRepository.save(company);
        log.info("Company created: {}", company);
        return new MessageResponse("Company created successfully");
    }

    @Override
    public MessageResponse deleteAgent(Long id) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agent not found"));
        agentRepository.delete(agent);
        log.info("Agent deleted: {}", agent);
        return new MessageResponse("Agent successfully deleted");
    }
}
