package com.sh32bit.airline.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AirportRequest {
    @NotBlank
    private String name;
    @NotNull
    private Long cityId;
}
