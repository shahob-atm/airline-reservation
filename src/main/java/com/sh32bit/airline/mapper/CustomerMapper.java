package com.sh32bit.airline.mapper;

import com.sh32bit.airline.entity.Customer;
import com.sh32bit.airline.response.CustomerResponse;

public class CustomerMapper {
    public static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .cityName(customer.getCity() == null ? null : customer.getCity().getName())
                .createdAt(customer.getCreatedAt())
                .updatedAt( customer.getUpdatedAt())
                .active(customer.isActive())
                .build();
    }
}
