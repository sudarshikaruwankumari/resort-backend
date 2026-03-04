// Reservation.java
package com.example.ocean_view_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;

    private String userId;          // Who created/made this reservation (staff or logged-in user)
    private String customerId;      // The actual guest / customer (may be different)

    private String roomId;

    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private double totalPrice;

    private String status;          // PENDING, CONFIRMED, CANCELLED, COMPLETED

    private LocalDate createdAt = LocalDate.now();
}