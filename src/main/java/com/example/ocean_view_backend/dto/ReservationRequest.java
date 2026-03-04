package com.example.ocean_view_backend.dto;

import java.time.LocalDate;

import lombok.Data;

// ReservationRequest.java  (input when creating)
@Data
public class ReservationRequest {
    private String roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    // Customer details (sent when creating reservation)
    private String customerFullName;
    private String customerEmail;
    private String customerPhone;
    // optional: nationality, passportId, etc.
}
