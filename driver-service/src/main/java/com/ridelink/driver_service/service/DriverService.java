package com.ridelink.driver_service.service;

import com.ridelink.driver_service.dto.DriverRequestDTO;
import com.ridelink.driver_service.dto.DriverResponseDTO;
import com.ridelink.driver_service.model.AvailabilityStatus;
import com.ridelink.driver_service.model.Location;
import java.util.List;

public interface DriverService {
    DriverResponseDTO createOrUpdateDriver(DriverRequestDTO request);
    DriverResponseDTO getDriverById(String driverId);
    DriverResponseDTO updateAvailability(String driverId, AvailabilityStatus availabilityStatus);
    DriverResponseDTO updateLocation(String driverId, Location location);
    List<DriverResponseDTO> getEligibleDrivers(String serviceArea, Double latitude, Double longitude, Double radiusKm);
}
