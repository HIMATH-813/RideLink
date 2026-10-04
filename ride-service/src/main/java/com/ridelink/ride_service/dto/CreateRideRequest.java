package com.ridelink.ride_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateRideRequest(
        @NotBlank String pickupLocation,
        @NotBlank String destination,
        @PositiveOrZero Double estimatedFare) {
}