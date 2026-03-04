package com.example.ocean_view_backend.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationResponse {
    private String id;

    // User (staff who created the reservation)
    private String userId;
    private String userUsername;

    // Customer (guest)
    private String customerId;
    private String customerFullName;
    private String customerEmail;
    private String customerPhone;

    // Room details (joined from Room table)
    private String roomId;
    private String roomName;
    private String roomImageUrl;           // ← this was missing! Add this line
    private double roomPricePerNight;

    // Reservation dates & calculated nights
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int nightsBooked;

    // Pricing & status
    private double totalPrice;
    private String status;
    private LocalDate createdAt;
}