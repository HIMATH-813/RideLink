package com.ridelink.ride_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateRideRequest(
        @NotNull @Positive Long passengerId,
        @NotBlank String pickupLocation,
        @NotBlank String destination,
        @PositiveOrZero Double estimatedFare) {
}
