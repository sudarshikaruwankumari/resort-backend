package com.example.ocean_view_backend.repository;

import com.example.ocean_view_backend.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {

    // Find overlapping reservations for a room in a date range
    @Query("{ 'roomId': ?0, " +
           " $or: [ " +
           "   { 'checkInDate': { $lt: ?2 }, 'checkOutDate': { $gt: ?1 } }," +
           "   { 'checkInDate': { $gte: ?1, $lt: ?2 } }," +
           "   { 'checkOutDate': { $gt: ?1, $lte: ?2 } }" +
           " ] }")
    List<Reservation> findOverlappingReservations(
            String roomId,
            LocalDate checkIn,
            LocalDate checkOut
    );

    // Find reservations for a specific user
    List<Reservation> findByUserId(String userId);
}