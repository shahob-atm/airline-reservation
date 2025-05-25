package com.sh32bit.airline.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightBatchUploadRequest {
    @NotEmpty(message = "Flights list must not be empty")
    @Valid
    private List<FlightRequest> flights;
}
