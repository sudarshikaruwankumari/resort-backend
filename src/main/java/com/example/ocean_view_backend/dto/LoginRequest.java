package com.example.ocean_view_backend.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String username;   // or email – your choice

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}