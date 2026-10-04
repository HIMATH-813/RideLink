package com.ridelink.driverservice.service;

import java.util.List;

import com.ridelink.driverservice.dto.DriverRequestDTO;
import com.ridelink.driverservice.dto.DriverResponseDTO;
import com.ridelink.driverservice.model.AvailabilityStatus;
import com.ridelink.driverservice.model.Location;

public interface DriverService {
    DriverResponseDTO createOrUpdateDriver(DriverRequestDTO request);
    DriverResponseDTO getDriverById(String driverId);
    DriverResponseDTO updateAvailability(String driverId, AvailabilityStatus availabilityStatus);
    DriverResponseDTO updateLocation(String driverId, Location location);
    List<DriverResponseDTO> getEligibleDrivers(String serviceArea, Double latitude, Double longitude, Double radiusKm);
}
