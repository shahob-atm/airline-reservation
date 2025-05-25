package com.sh32bit.airline.repository;

import com.sh32bit.airline.entity.Airport;
import com.sh32bit.airline.entity.Company;
import com.sh32bit.airline.entity.Flight;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    boolean existsByFromAndToAndCompanyAndDepartureTime(Airport from, Airport to, Company company,
                                                        @NotNull(message = "Departure time is required")
                                                        @Future(message = "Departure time must be in the future")
                                                        LocalDateTime departureTime);

    @Query("SELECT f FROM Flight f WHERE " +
            "(:fromAirportId IS NULL OR f.from.id = :fromAirportId) AND " +
            "(:toAirportId IS NULL OR f.to.id = :toAirportId) AND " +
            "FUNCTION('DATE', f.departureTime) = :date")
    List<Flight> findByDate(@Param("fromAirportId") Long fromAirportId,
                            @Param("toAirportId") Long toAirportId,
                            @Param("date") LocalDate date);

    @Query("SELECT f FROM Flight f WHERE " +
            "(:fromAirportId IS NULL OR f.from.id = :fromAirportId) AND " +
            "(:toAirportId IS NULL OR f.to.id = :toAirportId)")
    List<Flight> findWithoutDate(@Param("fromAirportId") Long fromAirportId,
                                 @Param("toAirportId") Long toAirportId);
}
