package com.sh32bit.airline.config;

import com.sh32bit.airline.entity.*;
import com.sh32bit.airline.enums.Role;
import com.sh32bit.airline.repository.AirportRepository;
import com.sh32bit.airline.repository.CityRepository;
import com.sh32bit.airline.repository.CompanyRepository;
import com.sh32bit.airline.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DemoDataLoader implements CommandLineRunner {

    private final CityRepository cityRepository;
    private final AirportRepository airportRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 1. CITY
        City tashkent = cityRepository.findByName("Tashkent")
                .orElseGet(() -> cityRepository.save(City.builder().name("Tashkent").build()));
        City istanbul = cityRepository.findByName("Istanbul")
                .orElseGet(() -> cityRepository.save(City.builder().name("Istanbul").build()));

        // 2. AIRPORT
        Airport tashkentAirport = airportRepository.findByName("Tashkent International Airport")
                .orElseGet(() -> airportRepository.save(Airport.builder().name("Tashkent International Airport").city(tashkent).build()));
        Airport istanbulAirport = airportRepository.findByName("Istanbul Airport")
                .orElseGet(() -> airportRepository.save(Airport.builder().name("Istanbul Airport").city(istanbul).build()));

        // 3. COMPANY
        Company uzAirways = companyRepository.findByName("UzAirways")
                .orElseGet(() -> companyRepository.save(Company.builder().name("UzAirways").build()));
        Company turkish = companyRepository.findByName("Turkish Airlines")
                .orElseGet(() -> companyRepository.save(Company.builder().name("Turkish Airlines").build()));

        // 4. ADMIN
        if (userRepository.findByEmail("admin@mail.com").isEmpty()) {
            Admin admin = Admin.builder()
                    .email("admin@mail.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("User")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            userRepository.save(admin);
        }

        // 5. AGENT
        if (userRepository.findByEmail("agent@mail.com").isEmpty()) {
            Agent agent = Agent.builder()
                    .email("agent@mail.com")
                    .password(passwordEncoder.encode("agent123"))
                    .firstName("Agent")
                    .lastName("Smith")
                    .role(Role.AGENT)
                    .active(true)
                    .company(uzAirways)
                    .airport(tashkentAirport)
                    .build();
            userRepository.save(agent);

            if (userRepository.findByEmail("agent2@mail.com").isEmpty()) {
                Agent agent2 = Agent.builder()
                        .email("agent2@mail.com")
                        .password(passwordEncoder.encode("agent123"))
                        .firstName("Kamol")
                        .lastName("Jamolov")
                        .role(Role.AGENT)
                        .active(true)
                        .company(turkish)
                        .airport(istanbulAirport)
                        .build();
                userRepository.save(agent2);
            }

            // 6. CUSTOMER
            if (userRepository.findByEmail("customer@mail.com").isEmpty()) {
                Customer customer = Customer.builder()
                        .email("customer@mail.com")
                        .password(passwordEncoder.encode("customer123"))
                        .firstName("Customer")
                        .lastName("Aliyev")
                        .role(Role.CUSTOMER)
                        .active(true)
                        .city(istanbul)
                        .build();
                userRepository.save(customer);
            }
        }
    }
}
