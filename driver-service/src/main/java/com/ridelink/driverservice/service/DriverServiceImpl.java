package com.ridelink.driverservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ridelink.driverservice.dto.DriverRequestDTO;
import com.ridelink.driverservice.dto.DriverResponseDTO;
import com.ridelink.driverservice.model.AvailabilityStatus;
import com.ridelink.driverservice.model.Driver;
import com.ridelink.driverservice.model.Location;
import com.ridelink.driverservice.repository.DriverRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {
    private final DriverRepository driverRepository;

    @Override
    public DriverResponseDTO createOrUpdateDriver(DriverRequestDTO request) {
        Driver driver = driverRepository.findByUserId(request.getUserId())
            .orElseGet(() -> Driver.builder()
                .driverId(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .build());

        driver.setServiceArea(request.getServiceArea());
        driver.setVehicleDetails(request.getVehicleDetails());
        driver.setAvailabilityStatus(request.getAvailabilityStatus() != null ? request.getAvailabilityStatus() : AvailabilityStatus.UNAVAILABLE);
        driver.setLocation(request.getLocation());
        if (request.getLocation() != null) {
            driver.getLocation().setUpdatedAt(java.time.Instant.now());
        }
        if (request.getRating() != null) {
            driver.setRating(request.getRating());
        }
        if (driver.getDriverId() == null) {
            driver.setDriverId(UUID.randomUUID().toString());
        }

        Driver savedDriver = driverRepository.save(driver);
        return mapToResponse(savedDriver);
    }

    @Override
    public DriverResponseDTO getDriverById(String driverId) {
        Driver driver = driverRepository.findByDriverId(driverId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found with id: " + driverId));
        return mapToResponse(driver);
    }

    @Override
    public DriverResponseDTO updateAvailability(String driverId, AvailabilityStatus availabilityStatus) {
        Driver driver = driverRepository.findByDriverId(driverId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found with id: " + driverId));
        driver.setAvailabilityStatus(availabilityStatus);
        return mapToResponse(driverRepository.save(driver));
    }

    @Override
    public DriverResponseDTO updateLocation(String driverId, Location location) {
        Driver driver = driverRepository.findByDriverId(driverId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found with id: " + driverId));
        if (location != null) {
            location.setUpdatedAt(java.time.Instant.now());
            driver.setLocation(location);
        }
        return mapToResponse(driverRepository.save(driver));
    }

    @Override
    public List<DriverResponseDTO> getEligibleDrivers(String serviceArea, Double latitude, Double longitude, Double radiusKm) {
        if (serviceArea == null || serviceArea.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service area is required");
        }

        List<Driver> drivers = driverRepository.findByAvailabilityStatusAndServiceAreaAndLocationIsNotNull(AvailabilityStatus.AVAILABLE, serviceArea);
        if (drivers == null || drivers.isEmpty()) {
            return new ArrayList<>();
        }

        if (latitude != null && longitude != null) {
            double maxDistanceKm = radiusKm != null ? radiusKm : 10.0;
            return drivers.stream()
                .filter(driver -> driver.getLocation() != null)
                .filter(driver -> calculateDistanceKm(latitude, longitude, driver.getLocation().getLatitude(), driver.getLocation().getLongitude()) <= maxDistanceKm)
                .map(this::mapToResponse)
                .toList();
        }

        return drivers.stream().map(this::mapToResponse).toList();
    }

    private DriverResponseDTO mapToResponse(Driver driver) {
        return DriverResponseDTO.builder()
            .id(driver.getId())
            .driverId(driver.getDriverId())
            .userId(driver.getUserId())
            .serviceArea(driver.getServiceArea())
            .vehicleDetails(driver.getVehicleDetails())
            .availabilityStatus(driver.getAvailabilityStatus())
            .location(driver.getLocation())
            .rating(driver.getRating())
            .createdAt(driver.getCreatedAt())
            .updatedAt(driver.getUpdatedAt())
            .build();
    }

    private double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        final int earthRadiusKm = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }
}
