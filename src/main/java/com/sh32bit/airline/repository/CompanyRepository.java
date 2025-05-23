package com.sh32bit.airline.repository;

import com.sh32bit.airline.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
