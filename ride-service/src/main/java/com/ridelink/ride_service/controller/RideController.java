package com.ridelink.ride_service.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader; // අලුතින් import කරන්න
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ridelink.ride_service.client.AccountServiceClient;
import com.ridelink.ride_service.dto.CreateRideRequest;
import com.ridelink.ride_service.dto.UpdateRideStatusRequest;
import com.ridelink.ride_service.model.Ride;
import com.ridelink.ride_service.service.RideService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // අලුතින් import කරන්න
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
    private final AccountServiceClient accountServiceClient;

    @PostMapping
    @Operation(summary = "Create a ride request")
    @SecurityRequirement(name = "Bearer Authentication") // Swagger එකට Token එක ඉල්ලීමට කියයි
    public ResponseEntity<Ride> createRide(
            @Valid @RequestBody CreateRideRequest request,
            @RequestHeader("Authorization") String authorizationHeader) { // HttpServletRequest වෙනුවට මෙය යොදන්න
        
        String passengerId = accountServiceClient.getCurrentUserId(authorizationHeader);

        Ride ride = rideService.createRide(request, passengerId);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{rideId}")
                .buildAndExpand(ride.getId())
                .toUri();
        return ResponseEntity.created(location).body(ride);
    }

    @PostMapping("/{rideId}/assign")
    @Operation(summary = "Assign an available driver to a ride")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Ride> assignDriver(@PathVariable String rideId) {
        return ResponseEntity.ok(rideService.assignDriver(rideId));
    }

    @GetMapping("/{rideId}")
    @Operation(summary = "Get a ride by its ID")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Ride> getRideById(@PathVariable String rideId) {
        return ResponseEntity.ok(rideService.getRideById(rideId));
    }

    @PatchMapping("/{rideId}/status")
    @Operation(summary = "Update a ride status")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<Ride> updateRideStatus(
            @PathVariable String rideId,
            @Valid @RequestBody UpdateRideStatusRequest request) {
        return ResponseEntity.ok(rideService.updateRideStatus(rideId, request.status()));
    }
}