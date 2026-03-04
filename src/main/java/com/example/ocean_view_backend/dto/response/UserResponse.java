package com.example.ocean_view_backend.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String email;          // if you have email in User model
    private String role;           // e.g. "ADMIN", "STAFF"
    private boolean enabled;       // active or not
    // Add any other safe fields — NEVER include password!
}