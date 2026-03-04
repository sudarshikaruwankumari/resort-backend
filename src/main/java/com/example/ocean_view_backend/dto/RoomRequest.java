package com.example.ocean_view_backend.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RoomRequest {
    private String name;                    // can be null
    private String description;             // can be null
    
    private Double pricePerNight;           // ← changed to Double (nullable)
    private Integer capacity;               // ← changed to Integer (nullable)
    
    private MultipartFile image;            // file upload
}