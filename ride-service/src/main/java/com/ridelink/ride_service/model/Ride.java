package com.ridelink.ride_service.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "rides")
public class Ride {
    @Id
    private String id;
    private Long passengerId;
    private Long driverId;
    private String pickupLocation;
    private String destination;
    private RideStatus status;
    private Double estimatedFare;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
