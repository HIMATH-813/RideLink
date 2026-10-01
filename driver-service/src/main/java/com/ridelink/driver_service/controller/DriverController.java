package com.ridelink.driver_service.controller;

import com.ridelink.driver_service.dto.DriverRequestDTO;
import com.ridelink.driver_service.dto.DriverResponseDTO;
import com.ridelink.driver_service.model.AvailabilityStatus;
import com.ridelink.driver_service.model.Location;
import com.ridelink.driver_service.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@Tag(name = "Driver & Vehicle Service", description = "Driver profile, availability, vehicle and eligible-driver management APIs")
public class DriverController {
    private final DriverService driverService;

    @Operation(summary = "Create or update a driver profile")
    @ApiResponse(responseCode = "201", description = "Driver created or updated successfully")
    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrUpdateDriver(@Valid @RequestBody DriverRequestDTO request) {
        DriverResponseDTO driver = driverService.createOrUpdateDriver(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(buildResponse(true, "Driver profile saved successfully", driver));
    }

    @Operation(summary = "Get driver by ID")
    @GetMapping("/{driverId}")
    public ResponseEntity<Map<String, Object>> getDriverById(@PathVariable String driverId) {
        DriverResponseDTO driver = driverService.getDriverById(driverId);
        return ResponseEntity.ok(buildResponse(true, "Driver profile retrieved successfully", driver));
    }

    @Operation(summary = "Update driver availability")
    @PutMapping("/{driverId}/availability")
    public ResponseEntity<Map<String, Object>> updateAvailability(
        @PathVariable String driverId,
        @RequestParam AvailabilityStatus availabilityStatus) {
        DriverResponseDTO driver = driverService.updateAvailability(driverId, availabilityStatus);
        return ResponseEntity.ok(buildResponse(true, "Availability updated successfully", driver));
    }

    @Operation(summary = "Update driver current location")
    @PutMapping("/{driverId}/location")
    public ResponseEntity<Map<String, Object>> updateLocation(@PathVariable String driverId, @Valid @RequestBody Location location) {
        DriverResponseDTO driver = driverService.updateLocation(driverId, location);
        return ResponseEntity.ok(buildResponse(true, "Location updated successfully", driver));
    }

    @Operation(summary = "Fetch eligible drivers by service area and optional location radius")
    @GetMapping("/eligible")
    public ResponseEntity<Map<String, Object>> getEligibleDrivers(
        @RequestParam String serviceArea,
        @RequestParam(required = false) Double latitude,
        @RequestParam(required = false) Double longitude,
        @RequestParam(required = false, defaultValue = "10.0") Double radiusKm) {
        List<DriverResponseDTO> drivers = driverService.getEligibleDrivers(serviceArea, latitude, longitude, radiusKm);
        return ResponseEntity.ok(buildResponse(true, "Eligible drivers retrieved successfully", drivers));
    }

    private Map<String, Object> buildResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return response;
    }
}
