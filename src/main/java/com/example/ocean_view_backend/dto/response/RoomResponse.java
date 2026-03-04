package com.example.ocean_view_backend.dto.response;

import lombok.Data;

@Data
public class RoomResponse {
    private String id;
    private String name;
    private String description;
    private double pricePerNight;
    private int capacity;
    private String imageUrl;
    private boolean available;
}