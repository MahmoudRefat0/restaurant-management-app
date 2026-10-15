package com.mahmoud.restaurant.service;

import com.mahmoud.restaurant.dto.UserRequest;
import com.mahmoud.restaurant.dto.UserResponse;
import com.mahmoud.restaurant.entity.User;
import com.mahmoud.restaurant.exception.BadRequestException;
import com.mahmoud.restaurant.exception.ResourceNotFoundException;
import com.mahmoud.restaurant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    public UserResponse create(UserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("Username is already taken");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFullName(request.fullName());
        user.setRole(request.role());

        return UserResponse.from(userRepository.save(user));
    }

    public void delete(Long id, String currentUsername) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getUsername().equals(currentUsername)) {
            throw new BadRequestException("You can't delete your own account");
        }
        userRepository.delete(user);
    }
}
