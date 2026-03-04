package com.example.ocean_view_backend.dto;

import lombok.Data;

@Data
public class ReservationStatusUpdate {
    private String status;  // e.g. "CONFIRMED", "CANCELLED", "COMPLETED"
}