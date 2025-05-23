package com.sh32bit.airline.repository;

import com.sh32bit.airline.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirportRepository extends JpaRepository<Airport, Long> {
}
