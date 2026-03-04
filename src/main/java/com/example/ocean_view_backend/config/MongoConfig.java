package com.example.ocean_view_backend.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Bean
    @Primary
    public MongoClient mongoClient() {
String uri = "mongodb+srv://sudarshika:12345678Ss__@cluster0.ydfqahu.mongodb.net/oceanview?retryWrites=true&w=majority&tls=true&tlsAllowInvalidCertificates=true";        
        System.out.println(">>> USING ATLAS CONNECTION: " + uri.substring(0, 50) + "..."); // partial log for security

        ConnectionString connectionString = new ConnectionString(uri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) {
        return new MongoTemplate(mongoClient, "oceanview"); // ← change database name if needed
    }
}