package com.sh32bit.airline.service;

import com.sh32bit.airline.request.AuthenticationRequest;
import com.sh32bit.airline.request.RegisterRequest;
import com.sh32bit.airline.response.AuthenticationResponse;
import com.sh32bit.airline.response.MessageResponse;

public interface AuthService {
    MessageResponse register(RegisterRequest request);

    AuthenticationResponse login(AuthenticationRequest request) throws Exception;
}
