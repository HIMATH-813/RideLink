package com.ridelink.driver_service.dto;

import com.ridelink.driver_service.model.AvailabilityStatus;
import com.ridelink.driver_service.model.Location;
import com.ridelink.driver_service.model.VehicleDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "DriverRequestDTO", description = "Request payload for a driver profile")
public class DriverRequestDTO {
    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Service area is required")
    private String serviceArea;

    @Valid
    @NotNull(message = "Vehicle details are required")
    private VehicleDetails vehicleDetails;

    @NotNull(message = "Availability status is required")
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.UNAVAILABLE;

    @Valid
    private Location location;

    @Schema(description = "Optional driver rating")
    private Double rating;
}
