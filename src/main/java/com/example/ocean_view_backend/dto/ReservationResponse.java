package com.example.ocean_view_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;


@Data
public class ReservationResponse {
    private String id;
    private String userId;
    private String userUsername;
    private String customerId;
    private String customerFullName;
    private String customerEmail;
    private String customerPhone;
    private String roomId;
    private String roomName;
    private String roomImageUrl;           // ← add this
    private double roomPricePerNight;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int nightsBooked;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt;
}