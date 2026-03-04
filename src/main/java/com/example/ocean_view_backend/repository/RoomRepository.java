package com.example.ocean_view_backend.repository;

import com.example.ocean_view_backend.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {
}