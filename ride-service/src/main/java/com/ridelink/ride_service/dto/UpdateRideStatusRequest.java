package com.ridelink.ride_service.dto;

import com.ridelink.ride_service.model.RideStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateRideStatusRequest(@NotNull RideStatus status) {
}
