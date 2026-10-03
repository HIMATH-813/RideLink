package com.ridelink.ride_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DriverResponse(
    String id,
    String driverId,
    String userId,
    String serviceArea,
    String availabilityStatus
) {}