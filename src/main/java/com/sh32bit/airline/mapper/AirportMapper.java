package com.sh32bit.airline.mapper;

import com.sh32bit.airline.entity.Airport;
import com.sh32bit.airline.response.AirportResponse;

public class AirportMapper {
    public static AirportResponse toResponse(Airport airport) {
        return AirportResponse.builder()
                .id(airport.getId())
                .name(airport.getName())
                .cityName(airport.getCity().getName())
                .build();
    }
}
