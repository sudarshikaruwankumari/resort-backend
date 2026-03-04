package com.example.ocean_view_backend.model;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;   // login name (or could be email)

    @Indexed(unique = true)
    private String email;

    private String password;   // hashed

    private String fullName;   // optional – good for resort system

    private Set<String> roles = new HashSet<>();   // e.g. ["ROLE_USER"], ["ROLE_ADMIN"]

    // You can add later: phone, address, isActive, etc.
}