
package com.ridelink.driverservice;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ridelink.driverservice.dto.DriverRequestDTO;
import com.ridelink.driverservice.model.AvailabilityStatus;
import com.ridelink.driverservice.model.Driver;
import com.ridelink.driverservice.model.Location;
import com.ridelink.driverservice.model.VehicleDetails;
import com.ridelink.driverservice.repository.DriverRepository;
import com.ridelink.driverservice.service.DriverServiceImpl;

class DriverServiceApplicationTests {
    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateDriverProfile() {
        DriverRequestDTO request = DriverRequestDTO.builder()
            .userId("user-1001")
            .serviceArea("Colombo")
            .vehicleDetails(VehicleDetails.builder()
                .model("Toyota Prius")
                .registrationNumber("ABC-1234")
                .vehicleType("CAR")
                .color("White")
                .capacity(4)
                .build())
            .availabilityStatus(AvailabilityStatus.AVAILABLE)
            .location(new Location(6.9271, 79.8612, Instant.now()))
            .build();

        Driver savedDriver = Driver.builder()
            .id("driver-1")
            .driverId("driver-uuid-1")
            .userId("user-1001")
            .serviceArea("Colombo")
            .vehicleDetails(request.getVehicleDetails())
            .availabilityStatus(AvailabilityStatus.AVAILABLE)
            .location(request.getLocation())
            .build();

        when(driverRepository.findByUserId("user-1001")).thenReturn(java.util.Optional.empty());
        when(driverRepository.save(any(Driver.class))).thenReturn(savedDriver);

        var result = driverService.createOrUpdateDriver(request);

        assertNotNull(result);
        assertEquals("driver-uuid-1", result.getDriverId());
        assertEquals("Colombo", result.getServiceArea());
        assertEquals(AvailabilityStatus.AVAILABLE, result.getAvailabilityStatus());
    }

    @Test
    void shouldReturnEligibleDriversForServiceArea() {
        Driver driver = Driver.builder()
            .id("driver-1")
            .driverId("driver-uuid-1")
            .userId("user-1001")
            .serviceArea("Colombo")
            .availabilityStatus(AvailabilityStatus.AVAILABLE)
            .location(new Location(6.9271, 79.8612, Instant.now()))
            .vehicleDetails(VehicleDetails.builder()
                .model("Honda Fit")
                .registrationNumber("XYZ-9988")
                .vehicleType("CAR")
                .color("Black")
                .capacity(4)
                .build())
            .build();

        when(driverRepository.findByAvailabilityStatusAndServiceAreaAndLocationIsNotNull(AvailabilityStatus.AVAILABLE, "Colombo"))
            .thenReturn(List.of(driver));

        var result = driverService.getEligibleDrivers("Colombo", 6.9271, 79.8612, 10.0);

        assertEquals(1, result.size());
        assertEquals("driver-uuid-1", result.get(0).getDriverId());
    }
}
