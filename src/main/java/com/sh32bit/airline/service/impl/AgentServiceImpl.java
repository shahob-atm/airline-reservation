package com.sh32bit.airline.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sh32bit.airline.entity.Agent;
import com.sh32bit.airline.entity.Airport;
import com.sh32bit.airline.entity.Company;
import com.sh32bit.airline.entity.Flight;
import com.sh32bit.airline.exception.ConflictException;
import com.sh32bit.airline.exception.NotFoundException;
import com.sh32bit.airline.mapper.FlightMapper;
import com.sh32bit.airline.repository.AgentRepository;
import com.sh32bit.airline.repository.AirportRepository;
import com.sh32bit.airline.repository.CompanyRepository;
import com.sh32bit.airline.repository.FlightRepository;
import com.sh32bit.airline.request.FlightBatchUploadRequest;
import com.sh32bit.airline.request.FlightRequest;
import com.sh32bit.airline.request.FlightUpdateRequest;
import com.sh32bit.airline.response.FlightResponse;
import com.sh32bit.airline.response.MessageResponse;
import com.sh32bit.airline.service.AgentService;
import com.sh32bit.airline.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    private final AirportRepository airportRepository;
    private final CompanyRepository companyRepository;
    private final AgentRepository agentRepository;
    private final FlightRepository flightRepository;
    private final EmailNotificationService emailNotificationService;

    @Override
    @Transactional
    public MessageResponse createFlight(FlightRequest request) {
        if (request.getArrivalTime().isBefore(request.getDepartureTime())) {
            throw new ConflictException("Arrival time must be after departure time");
        }

        validateFlightDate(request.getDepartureTime());
        validateFlightDate(request.getArrivalTime());

        if (request.getFromAirportId().equals(request.getToAirportId())) {
            throw new ConflictException("From Airport and To Airport are the same");
        }

        Airport from = getAirport(request.getFromAirportId());
        Airport to = getAirport(request.getToAirportId());
        Agent agent = getAgent(request.getAgentId());
        Company company = getCompany(request.getCompanyId());

        if (flightRepository.existsByFromAndToAndCompanyAndDepartureTime(
                from, to, company, request.getDepartureTime())) {
            throw new ConflictException("Flight already exists at this time!");
        }

        Flight flight = FlightMapper.toEntity(request);
        flight.setFrom(from);
        flight.setTo(to);
        flight.setAgent(agent);
        flight.setCompany(company);

        flightRepository.save(flight);
        log.info("Agent (id={}) created new flight: id={}, from={} to={} at {}",
                agent.getId(), flight.getId(), from.getName(), to.getName(), flight.getDepartureTime());

        return new MessageResponse("Flight created successfully");
    }

    @Override
    @Transactional
    public MessageResponse batchCreateFlights(FlightBatchUploadRequest request) {
        int count = 0, skipped = 0, error = 0;
        LocalDateTime now = LocalDateTime.now();
        List<String> errors = new ArrayList<>();
        for (FlightRequest f : request.getFlights()) {
            try {
                if (f.getDepartureTime().isBefore(now) || f.getArrivalTime().isBefore(now)) {
                    skipped++;
                    continue;
                }
                createFlight(f);
                count++;
            } catch (ConflictException e) {
                error++;
                errors.add(e.getMessage());
            } catch (Exception ex) {
                error++;
                errors.add("Other error: " + ex.getMessage());
            }
        }
        if (count == 0)
            throw new ConflictException("No valid (future) flights found in the batch. Errors: " + errors);
        String msg = count + " flights created, " + skipped + " past flights skipped, " + error + " errors.";
        if (!errors.isEmpty()) {
            msg += " Errors: " + String.join("; ", errors);
        }
        return new MessageResponse(msg);
    }

    @Override
    @Transactional
    public MessageResponse uploadFlights(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            ObjectMapper mapper = (filename != null && (filename.endsWith(".yaml") || filename.endsWith(".yml")))
                    ? new ObjectMapper(new YAMLFactory()) : new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            FlightBatchUploadRequest batchRequest = mapper.readValue(file.getInputStream(),
                    FlightBatchUploadRequest.class);
            return batchCreateFlights(batchRequest);
        } catch (Exception e) {
            log.error("Failed to parse uploaded file: {}", e.getMessage());
            throw new ConflictException("Failed to parse uploaded file: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MessageResponse updateFlight(Long flightId, FlightUpdateRequest request) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NotFoundException("Flight not found"));
        boolean changed = false;
        StringBuilder changes = new StringBuilder();

        // departureTime
        if (request.getDepartureTime() != null && !request.getDepartureTime().equals(flight.getDepartureTime())) {
            validateFlightDate(request.getDepartureTime());
            flight.setDepartureTime(request.getDepartureTime());
            changes.append("New Departure Time: ").append(request.getDepartureTime()).append("; ");
            changed = true;
        }

        // arrivalTime
        if (request.getArrivalTime() != null && !request.getArrivalTime().equals(flight.getArrivalTime())) {
            validateFlightDate(request.getArrivalTime());
            flight.setArrivalTime(request.getArrivalTime());
            changes.append("New Arrival Time: ").append(request.getArrivalTime()).append("; ");
            changed = true;
        }

        // price
        if (request.getPrice() != null && !request.getPrice().equals(flight.getPrice())) {
            flight.setPrice(request.getPrice());
            changes.append("New Price: ").append(request.getPrice()).append("; ");
            changed = true;
        }

        // totalSeats (if allowed)
        if (request.getTotalSeats() != null && !request.getTotalSeats().equals(flight.getTotalSeats())) {
            if (request.getTotalSeats() < (flight.getTotalSeats() - flight.getAvailableSeats())) {
                throw new ConflictException("Total seats cannot be less than tickets already sold");
            }
            flight.setTotalSeats(request.getTotalSeats());
            int newAvailable = request.getTotalSeats() - (flight.getTotalSeats() - flight.getAvailableSeats());
            flight.setAvailableSeats(newAvailable);
            changes.append("New Total Seats: ").append(request.getTotalSeats()).append("; ");
            changed = true;
        }

        flightRepository.save(flight);

        if (changed) {
            flightRepository.save(flight);
            emailNotificationService.sendFlightUpdateNotification(flight, "Flight updated: " + changes);
            log.info("Flight {} updated by agent, changes: {}", flightId, changes);
            return new MessageResponse("Flight updated, all customers notified.");
        } else {
            return new MessageResponse("No changes were made.");
        }
    }

    @Override
    public List<FlightResponse> getAgentFlights(Long companyId) {
        List<Flight> flights = (companyId != null)
                ? flightRepository.findAll().stream().filter(f -> f.getCompany().getId().equals(companyId)).toList()
                : flightRepository.findAll();
        return flights.stream().map(FlightMapper::fromEntity).collect(Collectors.toList());
    }

    private void validateFlightDate(LocalDateTime date) {
        if (date.isBefore(LocalDateTime.now())) {
            throw new ConflictException("Flight date must be in the future");
        }
    }

    private Airport getAirport(Long id) {
        return airportRepository.findById(id).orElseThrow(() -> new NotFoundException("Airport not found"));
    }

    private Company getCompany(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new NotFoundException("Company not found"));
    }

    private Agent getAgent(Long id) {
        return agentRepository.findById(id).orElseThrow(() -> new NotFoundException("Agent not found"));
    }
}
