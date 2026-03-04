package com.example.ocean_view_backend.controller;

import com.example.ocean_view_backend.dto.ReservationRequest;
import com.example.ocean_view_backend.dto.ReservationStatusUpdate;
import com.example.ocean_view_backend.dto.response.ReservationResponse;
import com.example.ocean_view_backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationRequest request) {
        try {
            ReservationResponse response = reservationService.createReservation(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ReservationResponse() {{
                        setStatus("ERROR");
                    }});
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        List<ReservationResponse> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getReservationById(@PathVariable String id) {
        ReservationResponse response = reservationService.getReservationById(id);
        return ResponseEntity.ok(response);
    }

    // ── THIS IS THE MISSING ENDPOINT THAT FIXES THE CORS/404 ERROR ──────────
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @RequestParam String roomId,
            @RequestParam String checkIn,
            @RequestParam String checkOut) {

        boolean available = reservationService.isRoomAvailable(roomId, checkIn, checkOut);

        if (available) {
            return ResponseEntity.ok(Map.of("available", true));
        } else {
            return ResponseEntity.ok(Map.of(
                "available", false,
                "message", "Room is already booked for the selected dates"
            ));
        }
    }


    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationResponse> updateReservationStatus(
            @PathVariable String id,
            @RequestBody ReservationStatusUpdate request) {

        ReservationResponse updated = reservationService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(updated);
    }
}