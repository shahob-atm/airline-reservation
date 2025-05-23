package com.sh32bit.airline.repository;

import com.sh32bit.airline.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
