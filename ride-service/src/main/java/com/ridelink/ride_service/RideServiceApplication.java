package com.ridelink.ride_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RideServiceApplication {

    public static void main(String[] args) {
        // .env file loading and setting environment variables
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        String mongodbUri = dotenv.get("MONGODB_URI");
        if (mongodbUri == null || mongodbUri.isBlank()) {
            throw new IllegalStateException("MONGODB_URI must be set in the .env file");
        }
        System.setProperty("MONGODB_URI", mongodbUri);

        dotenv.entries().forEach(entry -> {
            if (!"MONGODB_URI".equals(entry.getKey())) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
            // Debuging and logging the loaded environment variables
            // System.out.println("Loaded ENV -> " + entry.getKey() + ": " + entry.getValue());
        });

        SpringApplication.run(RideServiceApplication.class, args);
    }
}