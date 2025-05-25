package com.sh32bit.airline.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;
    private String companyName;
    private String airportName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
