package com.example.ocean_view_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "rooms")
public class Room {

    @Id
    private String id;

    private String name;            // e.g. "Deluxe Ocean View"
    private String description;
    private double pricePerNight;
    private int capacity;           // max people
    private String imageUrl;        // URL from ImgBB
    private boolean available = true;
}