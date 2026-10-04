package com.ridelink.ride_service.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ridelink.ride_service.dto.ApiResponse;
import com.ridelink.ride_service.dto.DriverResponse;

@FeignClient(name = "driver-service", url = "${DRIVER_SERVICE_URL:http://localhost:8082}")
public interface DriverServiceClient {

    @GetMapping("/api/drivers/eligible")
    ApiResponse<List<DriverResponse>> getEligibleDrivers(@RequestParam("serviceArea") String serviceArea);

    @PutMapping("/api/drivers/{driverId}/availability")
    ApiResponse<DriverResponse> updateAvailability(
            @PathVariable("driverId") String driverId,
            @RequestParam("availabilityStatus") String availabilityStatus);
}