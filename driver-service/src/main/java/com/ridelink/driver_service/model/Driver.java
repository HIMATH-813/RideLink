package com.ridelink.driver_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "drivers")
@Schema(name = "Driver", description = "Driver profile and operational data")
public class Driver {
    @Id
    private String id;

    @Indexed(unique = true)
    @Schema(description = "Public driver identifier")
    private String driverId;

    @Indexed(unique = true)
    @Schema(description = "User identifier from the authentication service")
    private String userId;

    @Schema(description = "Vehicle information")
    private VehicleDetails vehicleDetails;

    @Schema(description = "Driver availability state")
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.UNAVAILABLE;

    @Schema(description = "Current service area")
    private String serviceArea;

    @Schema(description = "Current driver location")
    private Location location;

    @Schema(description = "Driver rating")
    private Double rating = 0.0;

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant updatedAt;
}
