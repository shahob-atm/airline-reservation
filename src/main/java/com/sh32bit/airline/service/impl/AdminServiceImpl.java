package com.sh32bit.airline.service.impl;

import com.sh32bit.airline.entity.*;
import com.sh32bit.airline.exception.ConflictException;
import com.sh32bit.airline.exception.NotFoundException;
import com.sh32bit.airline.mapper.AgentMapper;
import com.sh32bit.airline.mapper.CustomerMapper;
import com.sh32bit.airline.repository.*;
import com.sh32bit.airline.request.AgentRequest;
import com.sh32bit.airline.request.AirportRequest;
import com.sh32bit.airline.request.CityRequest;
import com.sh32bit.airline.request.CompanyRequest;
import com.sh32bit.airline.response.AgentResponse;
import com.sh32bit.airline.response.CustomerResponse;
import com.sh32bit.airline.response.MessageResponse;
import com.sh32bit.airline.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final CityRepository cityRepository;
    private final AirportRepository airportRepository;
    private final CompanyRepository companyRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @Override
    public MessageResponse createCity(CityRequest request) {
        log.info("Admin requests to create city: {}", request.getName());
        if (cityRepository.findAll().stream().anyMatch(c -> c.getName().equalsIgnoreCase(request.getName())))
            throw new ConflictException("City with this name already exists");
        City city = City.builder().name(request.getName()).build();
        cityRepository.save(city);
        log.debug("City created: {}", city.getName());
        return new MessageResponse("City created success");
    }

    @Override
    public MessageResponse createAirport(AirportRequest request) {
        log.info("Admin requests to create airport: {} in cityId={}", request.getName(), request.getCityId());
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new NotFoundException("City not found"));
        Airport airport = Airport.builder()
                .name(request.getName())
                .city(city)
                .build();
        airportRepository.save(airport);
        log.debug("Airport created: {} (city: {})", airport.getName(), city.getName());
        return new MessageResponse("Airport created successfully");
    }

    @Override
    public MessageResponse createCompany(CompanyRequest request) {
        log.info("Admin requests to create company: {}", request.getName());
        if (companyRepository.findAll().stream().anyMatch(c -> c.getName().equalsIgnoreCase(request.getName())))
            throw new ConflictException("Company with this name already exists");
        Company company = Company.builder().name(request.getName()).build();
        companyRepository.save(company);
        log.debug("Company created: {}", company.getName());
        return new MessageResponse("Company created successfully");
    }

    @Override
    @Transactional
    public MessageResponse createAgent(AgentRequest request) {
        log.info("Admin requests to create agent: {} {} for companyId={} airportId={}",
                request.getFirstName(), request.getLastName(), request.getCompanyId(), request.getAirportId());

        if (agentRepository.existsByCompanyIdAndAirportId(request.getCompanyId(), request.getAirportId())) {
            throw new ConflictException("This company already has an agent at this airport!");
        }

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new NotFoundException("Company not found"));

        Airport airport = airportRepository.findById(request.getAirportId())
                .orElseThrow(() -> new NotFoundException("Airport not found"));

        if (userRepository.findByEmail(request.getEmail()).isPresent())
            throw new ConflictException("Email is already in use");

        Agent agent = AgentMapper.toAgent(request);
        agent.setCompany(company);
        agent.setAirport(airport);
        agent.setPassword(passwordEncoder.encode(request.getPassword()));

        agentRepository.save(agent);
        log.debug("Agent created: {} {} (email: {})", agent.getFirstName(), agent.getLastName(), agent.getEmail());
        return new MessageResponse("Agent created successfully");
    }

    @Override
    @Transactional
    public MessageResponse blockCustomer(Long customerId) {
        log.info("Admin requests to block customerId={}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        if (!customer.isActive()) {
            throw new ConflictException("Customer is already blocked");
        }
        customer.setActive(false);
        customerRepository.save(customer);
        log.warn("Customer blocked: {} ({})", customer.getId(), customer.getEmail());
        return new MessageResponse("Customer blocked");
    }

    @Override
    @Transactional
    public MessageResponse unblockCustomer(Long customerId) {
        log.info("Admin requests to unblock customerId={}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));
        if (customer.isActive()) {
            throw new ConflictException("Customer is already active");
        }
        customer.setActive(true);
        customerRepository.save(customer);
        log.warn("Customer unblocked: {} ({})", customer.getId(), customer.getEmail());
        return new MessageResponse("Customer unblocked");
    }

    @Override
    @Transactional
    public MessageResponse blockAgent(Long agentId) {
        log.info("Admin requests to block agentId={}", agentId);
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new NotFoundException("Agent not found"));
        if (!agent.isActive()) {
            throw new ConflictException("Agent is already blocked");
        }
        agent.setActive(false);
        agentRepository.save(agent);
        log.warn("Agent blocked: {} ({})", agent.getId(), agent.getEmail());
        return new MessageResponse("Agent blocked");
    }

    @Override
    @Transactional
    public MessageResponse unblockAgent(Long agentId) {
        log.info("Admin requests to unblock agentId={}", agentId);
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new NotFoundException("Agent not found"));
        if (agent.isActive()) {
            throw new ConflictException("Agent is already active");
        }
        agent.setActive(true);
        agentRepository.save(agent);
        log.warn("Agent unblocked: {} ({})", agent.getId(), agent.getEmail());
        return new MessageResponse("Agent unblocked");
    }

    @Override
    public List<AgentResponse> getAllAgents() {
        return agentRepository.findAll().stream()
                .map(AgentMapper::toAgentResponse).toList();
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerMapper::toCustomerResponse).toList();
    }
}
