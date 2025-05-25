package com.sh32bit.airline.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CitySelectRequest {
    @NotNull
    private Long cityId;
}
