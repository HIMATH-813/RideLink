package com.ridelink.driverservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "VehicleDetails", description = "Vehicle profile information")
public class VehicleDetails {
    @NotBlank(message = "Vehicle model is required")
    private String model;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    @NotBlank(message = "Vehicle color is required")
    private String color;

    @Positive(message = "Capacity must be greater than zero")
    private Integer capacity;
}
