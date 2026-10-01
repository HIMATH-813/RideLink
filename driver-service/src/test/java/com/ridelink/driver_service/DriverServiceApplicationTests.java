<<<<<<< HEAD
package com.ridelink.driver_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DriverServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
=======
package com.ridelink.driver_service;

import com.ridelink.driver_service.dto.DriverRequestDTO;
import com.ridelink.driver_service.model.AvailabilityStatus;
import com.ridelink.driver_service.model.Driver;
import com.ridelink.driver_service.model.Location;
import com.ridelink.driver_service.model.VehicleDetails;
import com.ridelink.driver_service.repository.DriverRepository;
import com.ridelink.driver_service.service.DriverServiceImpl;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
>>>>>>> 7b0870a86b0f2ca10d6ed9918f6a4b9073cd202a
