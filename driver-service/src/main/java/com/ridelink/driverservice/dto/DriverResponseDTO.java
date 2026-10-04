package com.ridelink.driverservice.dto;

import com.ridelink.driverservice.model.AvailabilityStatus;
import com.ridelink.driverservice.model.Location;
import com.ridelink.driverservice.model.VehicleDetails;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "DriverResponseDTO", description = "Driver response payload")
public class DriverResponseDTO {
    private String id;
    private String driverId;
    private String userId;
    private String serviceArea;
    private VehicleDetails vehicleDetails;
    private AvailabilityStatus availabilityStatus;
    private Location location;
    private Double rating;
    private Instant createdAt;
    private Instant updatedAt;
}
