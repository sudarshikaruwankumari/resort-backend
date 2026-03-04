package com.example.ocean_view_backend.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.ocean_view_backend.dto.ReservationRequest;
import com.example.ocean_view_backend.dto.response.ReservationResponse;
import com.example.ocean_view_backend.model.Customer;
import com.example.ocean_view_backend.model.Reservation;
import com.example.ocean_view_backend.model.Room;
import com.example.ocean_view_backend.model.User;
import com.example.ocean_view_backend.repository.CustomerRepository;
import com.example.ocean_view_backend.repository.ReservationRepository;
import com.example.ocean_view_backend.repository.RoomRepository;
import com.example.ocean_view_backend.repository.UserRepository;

@Service
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private UserRepository userRepository;

    public ReservationResponse createReservation(ReservationRequest request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (request.getCheckInDate().isAfter(request.getCheckOutDate()) ||
            request.getCheckInDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Invalid check-in / check-out dates");
        }

        List<Reservation> overlapping = reservationRepository.findOverlappingReservations(
                request.getRoomId(), request.getCheckInDate(), request.getCheckOutDate());

        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Room is already booked for the selected dates");
        }

        Customer customer = new Customer();
        customer.setFullName(request.getCustomerFullName());
        customer.setEmail(request.getCustomerEmail());
        customer.setPhone(request.getCustomerPhone());
        customer = customerRepository.save(customer);

        long nights = ChronoUnit.DAYS.between(request.getCheckInDate(), request.getCheckOutDate());
        double totalPrice = nights * room.getPricePerNight();

        Reservation reservation = new Reservation();
        reservation.setUserId(currentUser.getId());
        reservation.setCustomerId(customer.getId());
        reservation.setRoomId(room.getId());
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setTotalPrice(totalPrice);
        reservation.setStatus("PENDING");

        reservation = reservationRepository.save(reservation);

        return mapToResponse(reservation, currentUser, customer, room);
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(r -> {
                    User user = userRepository.findById(r.getUserId()).orElse(null);
                    Customer cust = customerRepository.findById(r.getCustomerId()).orElse(null);
                    Room room = roomRepository.findById(r.getRoomId()).orElse(null);
                    return mapToResponse(r, user, cust, room);
                })
                .collect(Collectors.toList());
    }

    public ReservationResponse getReservationById(String id) {
        Reservation r = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));

        User user = userRepository.findById(r.getUserId()).orElse(null);
        Customer cust = customerRepository.findById(r.getCustomerId()).orElse(null);
        Room room = roomRepository.findById(r.getRoomId()).orElse(null);

        return mapToResponse(r, user, cust, room);
    }

    public boolean isRoomAvailable(String roomId, String checkInStr, String checkOutStr) {
        LocalDate checkIn = LocalDate.parse(checkInStr);
        LocalDate checkOut = LocalDate.parse(checkOutStr);

        List<Reservation> overlapping = reservationRepository
                .findOverlappingReservations(roomId, checkIn, checkOut);

        return overlapping.isEmpty();
    }

    public ReservationResponse updateStatus(String id, String newStatus) {
        Reservation res = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        String upperStatus = newStatus.toUpperCase();
        if (!List.of("PENDING", "CONFIRMED", "CANCELLED", "COMPLETED", "HANDOVERED").contains(upperStatus)) {
            throw new RuntimeException("Invalid status");
        }

        res.setStatus(upperStatus);
        Reservation updated = reservationRepository.save(res);

        User user = userRepository.findById(res.getUserId()).orElse(null);
        Customer cust = customerRepository.findById(res.getCustomerId()).orElse(null);
        Room room = roomRepository.findById(res.getRoomId()).orElse(null);

        return mapToResponse(updated, user, cust, room);
    }

    /**
     * Maps Reservation → ReservationResponse with FULL joined data
     * - Includes room name, imageUrl, pricePerNight
     * - Calculates nightsBooked
     * - Safe null handling
     */
    private ReservationResponse mapToResponse(Reservation r, User user, Customer customer, Room room) {
    ReservationResponse res = new ReservationResponse();
    res.setId(r.getId());

    // User (staff/admin who created)
    res.setUserId(r.getUserId());
    res.setUserUsername(user != null ? user.getUsername() : "Unknown");

    // Customer (guest)
    res.setCustomerId(r.getCustomerId());
    res.setCustomerFullName(customer != null ? customer.getFullName() : "N/A");
    res.setCustomerEmail(customer != null ? customer.getEmail() : "N/A");
    res.setCustomerPhone(customer != null ? customer.getPhone() : "N/A");

    // Room details - now fully mapped
    res.setRoomId(r.getRoomId());
    res.setRoomName(room != null ? room.getName() : "Unknown Room");
    res.setRoomImageUrl(room != null ? room.getImageUrl() : null);   // ← this line is now valid
    res.setRoomPricePerNight(room != null ? room.getPricePerNight() : 0.0);

    // Dates & nights
    res.setCheckInDate(r.getCheckInDate());
    res.setCheckOutDate(r.getCheckOutDate());
    long nights = ChronoUnit.DAYS.between(r.getCheckInDate(), r.getCheckOutDate());
    res.setNightsBooked((int) nights);

    // Total price
    res.setTotalPrice(r.getTotalPrice());

    res.setStatus(r.getStatus());
    res.setCreatedAt(r.getCreatedAt());

    return res;
}
}