package com.sh32bit.airline.service.impl;

import com.sh32bit.airline.entity.Customer;
import com.sh32bit.airline.enums.Role;
import com.sh32bit.airline.exception.EmailAlreadyExistsException;
import com.sh32bit.airline.exception.PasswordMismatchException;
import com.sh32bit.airline.repository.UserRepository;
import com.sh32bit.airline.request.AuthenticationRequest;
import com.sh32bit.airline.request.RegisterRequest;
import com.sh32bit.airline.response.AuthenticationResponse;
import com.sh32bit.airline.response.MessageResponse;
import com.sh32bit.airline.security.AppUserDetails;
import com.sh32bit.airline.service.AuthService;
import com.sh32bit.airline.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public MessageResponse register(RegisterRequest request) {
        log.info("Register attempt: {}", request.getEmail());
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            log.warn("Password mismatch for email: {}", request.getEmail());
            throw new PasswordMismatchException();
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            log.warn("Duplicate email registration attempt: {}", request.getEmail());
            throw new EmailAlreadyExistsException();
        }

        Customer user = Customer.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.CUSTOMER)
                .build();

        userRepository.save(user);
        log.info("New user registered: email={}, role={}", user.getEmail(), user.getRole());
        return new MessageResponse("success");
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) throws Exception {
        log.info("Login requested for email: {}", request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        log.info("Login successful: userId={}, role={}", userDetails.getUser().getId(), userDetails.getUser().getRole());
        return new AuthenticationResponse(accessToken, refreshToken);
    }
}
