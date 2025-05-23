package com.sh32bit.airline.repository;

import com.sh32bit.airline.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    boolean existsByCompanyIdAndAirportId(Long companyId, Long airportId);
}
