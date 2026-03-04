package com.example.ocean_view_backend.controller;

import com.example.ocean_view_backend.dto.RoomRequest;
import com.example.ocean_view_backend.dto.response.RoomResponse;
import com.example.ocean_view_backend.model.Room;
import com.example.ocean_view_backend.repository.RoomRepository;
import com.example.ocean_view_backend.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
 

public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    // Get all rooms (public)
    @GetMapping
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get one room (public)
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable String id) {
        Optional<Room> room = roomRepository.findById(id);
        return room.map(r -> ResponseEntity.ok(toResponse(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Create room (ADMIN only) + image upload
    @PostMapping
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<RoomResponse> createRoom(@ModelAttribute RoomRequest request) throws IOException {
    Room room = new Room();

    room.setName(request.getName() != null ? request.getName() : "Unnamed Room");
    room.setDescription(request.getDescription() != null ? request.getDescription() : null);
    room.setPricePerNight(request.getPricePerNight() != null ? request.getPricePerNight() : 0.0);
    room.setCapacity(request.getCapacity() != null ? request.getCapacity() : 0);

    if (request.getImage() != null && !request.getImage().isEmpty()) {
        String imageUrl = imageUploadService.uploadImage(request.getImage());
        room.setImageUrl(imageUrl);
    }

    Room saved = roomRepository.save(room);
    return ResponseEntity.ok(toResponse(saved));
}

  @PutMapping("/{id}")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<RoomResponse> updateRoom(
        @PathVariable String id,
        @ModelAttribute RoomRequest request) throws IOException {

    Optional<Room> optionalRoom = roomRepository.findById(id);
    if (optionalRoom.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Room room = optionalRoom.get();

    // Update ONLY if field is provided (not null)
    if (request.getName() != null) {
        room.setName(request.getName());
    }

    if (request.getDescription() != null) {
        room.setDescription(request.getDescription());
    }

    if (request.getPricePerNight() != null) {
        room.setPricePerNight(request.getPricePerNight());
    }

    if (request.getCapacity() != null) {
        room.setCapacity(request.getCapacity());
    }

    // Image - only update if new file
    if (request.getImage() != null && !request.getImage().isEmpty()) {
        String imageUrl = imageUploadService.uploadImage(request.getImage());
        room.setImageUrl(imageUrl);
    }

    // Save once
    Room updated = roomRepository.save(room);

    return ResponseEntity.ok(toResponse(updated));
}

    // Delete room (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        if (!roomRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        roomRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private RoomResponse toResponse(Room room) {
        RoomResponse res = new RoomResponse();
        res.setId(room.getId());
        res.setName(room.getName());
        res.setDescription(room.getDescription());
        res.setPricePerNight(room.getPricePerNight());
        res.setCapacity(room.getCapacity());
        res.setImageUrl(room.getImageUrl());
        res.setAvailable(room.isAvailable());
        return res;
    }
}