package com.sh32bit.airline.repository;

import com.sh32bit.airline.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
