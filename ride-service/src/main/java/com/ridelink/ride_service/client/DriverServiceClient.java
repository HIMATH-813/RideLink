package com.ridelink.ride_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ridelink.ride_service.dto.DriverResponse;
import com.ridelink.ride_service.dto.DriverStatusUpdateRequest;

@FeignClient(name = "driver-service", url = "${DRIVER_SERVICE_URL:http://localhost:8082}")
public interface DriverServiceClient {
    @GetMapping("/api/drivers/available")
    DriverResponse findAvailableDriver(@RequestParam("pickupLocation") String pickupLocation);

    @PatchMapping("/api/drivers/{driverId}/status")
    void updateDriverStatus(
            @PathVariable("driverId") Long driverId,
            @RequestBody DriverStatusUpdateRequest request);
}
