package com.sh32bit.airline.repository;

import com.sh32bit.airline.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AirportRepository extends JpaRepository<Airport, Long> {
    Optional<Airport> findByName(String name);
    List<Airport> findAllByCityId(Long cityId);
}
