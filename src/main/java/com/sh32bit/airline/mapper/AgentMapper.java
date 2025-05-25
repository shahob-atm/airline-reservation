package com.sh32bit.airline.mapper;

import com.sh32bit.airline.entity.Agent;
import com.sh32bit.airline.request.AgentRequest;
import com.sh32bit.airline.response.AgentResponse;

public class AgentMapper {
    public static AgentResponse toAgentResponse(Agent agent) {
        return AgentResponse.builder()
                .id(agent.getId())
                .email(agent.getEmail())
                .firstName(agent.getFirstName())
                .lastName(agent.getLastName())
                .active(agent.isActive())
                .companyName(agent.getCompany() != null ? agent.getCompany().getName() : null)
                .airportName(agent.getAirport() != null ? agent.getAirport().getName() : null)
                .createdAt(agent.getCreatedAt())
                .updatedAt(agent.getUpdatedAt())
                .build();
    }

    public static Agent toAgent(AgentRequest request) {
        return Agent.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .active(true)
                .build();
    }
}
