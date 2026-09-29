package com.ridelink.ride_service.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ridelink.ride_service.dto.CreateRideRequest;
import com.ridelink.ride_service.dto.UpdateRideStatusRequest;
import com.ridelink.ride_service.model.Ride;
import com.ridelink.ride_service.service.RideService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rides")
@Validated
@RequiredArgsConstructor
@Tag(name = "Ride Management", description = "Create and manage rides")
public class RideController {
    private final RideService rideService;

    @PostMapping
    @Operation(summary = "Create a ride request")
    public ResponseEntity<Ride> createRide(@Valid @RequestBody CreateRideRequest request) {
        Ride ride = rideService.createRide(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{rideId}")
                .buildAndExpand(ride.getId())
                .toUri();
        return ResponseEntity.created(location).body(ride);
    }

    @PostMapping("/{rideId}/assign")
    @Operation(summary = "Assign an available driver to a ride")
    public ResponseEntity<Ride> assignDriver(@PathVariable String rideId) {
        return ResponseEntity.ok(rideService.assignDriver(rideId));
    }

    @GetMapping("/{rideId}")
    @Operation(summary = "Get a ride by its ID")
    public ResponseEntity<Ride> getRideById(@PathVariable String rideId) {
        return ResponseEntity.ok(rideService.getRideById(rideId));
    }

    @PatchMapping("/{rideId}/status")
    @Operation(summary = "Update a ride status")
    public ResponseEntity<Ride> updateRideStatus(
            @PathVariable String rideId,
            @Valid @RequestBody UpdateRideStatusRequest request) {
        return ResponseEntity.ok(rideService.updateRideStatus(rideId, request.status()));
    }
}
