package com.example.ocean_view_backend.controller;

import com.example.ocean_view_backend.dto.response.UserResponse;
import com.example.ocean_view_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
   // Only admins should see all users
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * GET /api/users
     * Returns list of all users (staff/admins) without passwords
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}