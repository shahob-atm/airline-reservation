package com.sh32bit.airline.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityRequest {
    @NotBlank
    private String name;
}
