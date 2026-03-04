package com.example.ocean_view_backend.service;

import com.example.ocean_view_backend.dto.response.UserResponse;
import com.example.ocean_view_backend.model.User;
import com.example.ocean_view_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all users (staff/admins) without sensitive data
     * - Only safe fields: id, username, role, etc.
     * - Recommended: restrict to ADMIN only
     */
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> {
                    UserResponse dto = new UserResponse();
                    dto.setId(user.getId());
                    dto.setUsername(user.getUsername());
                    dto.setEmail(user.getEmail() != null ? user.getEmail() : "N/A");
                     
                    return dto;
                })
                .collect(Collectors.toList());
    }
}