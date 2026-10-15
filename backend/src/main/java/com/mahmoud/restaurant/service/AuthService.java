package com.mahmoud.restaurant.service;

import com.mahmoud.restaurant.dto.AuthResponse;
import com.mahmoud.restaurant.dto.LoginRequest;
import com.mahmoud.restaurant.dto.UserResponse;
import com.mahmoud.restaurant.entity.User;
import com.mahmoud.restaurant.exception.ResourceNotFoundException;
import com.mahmoud.restaurant.repository.UserRepository;
import com.mahmoud.restaurant.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = findUser(userDetails.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), user.getFullName(), user.getRole());
    }

    public UserResponse getProfile(String username) {
        return UserResponse.from(findUser(username));
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
