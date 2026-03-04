// Customer.java  (simple embedded or separate document)
package com.example.ocean_view_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    private String fullName;
    private String email;
    private String phone;
    private String nationality;     // optional
    private String passportId;      // optional
}