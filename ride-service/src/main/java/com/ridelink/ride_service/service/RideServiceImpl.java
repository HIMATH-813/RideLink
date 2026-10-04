package com.ridelink.ride_service.service;

import java.time.LocalDateTime;
import java.util.List;

import feign.FeignException;
import org.springframework.stereotype.Service;

import com.ridelink.ride_service.client.DriverServiceClient;
import com.ridelink.ride_service.dto.ApiResponse;
import com.ridelink.ride_service.dto.CreateRideRequest;
import com.ridelink.ride_service.dto.DriverResponse;
import com.ridelink.ride_service.exception.InvalidStateTransitionException;
import com.ridelink.ride_service.exception.NoAvailableDriverException;
import com.ridelink.ride_service.exception.RideNotFoundException;
import com.ridelink.ride_service.model.Ride;
import com.ridelink.ride_service.model.RideStatus;
import com.ridelink.ride_service.repository.RideRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {
    private static final String DRIVER_BUSY = "BUSY";
    private static final String DRIVER_AVAILABLE = "AVAILABLE";

    private final RideRepository rideRepository;
    private final DriverServiceClient driverServiceClient;

    @Override
    public Ride createRide(CreateRideRequest request) {
        LocalDateTime now = LocalDateTime.now();
        Ride ride = Ride.builder()
                .passengerId(request.passengerId())
                .pickupLocation(request.pickupLocation())
                .destination(request.destination())
                .estimatedFare(request.estimatedFare())
                .status(RideStatus.REQUESTED)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return rideRepository.save(ride);
    }

    @Override
    public Ride assignDriver(String rideId) {
        Ride ride = getRideById(rideId);

        // Ensure the ride is in REQUESTED state before assigning
        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidStateTransitionException(ride.getStatus(), RideStatus.ASSIGNED);
        }

        ApiResponse<List<DriverResponse>> apiResponse;
        try {
            // Fetch eligible drivers from Driver Service based on the pickup location
            apiResponse = driverServiceClient.getEligibleDrivers(ride.getPickupLocation());
        } catch (FeignException.NotFound exception) {
            throw new NoAvailableDriverException(ride.getPickupLocation());
        }

        // Check if the response is valid and contains available drivers
        if (apiResponse == null || !apiResponse.success() || apiResponse.data() == null || apiResponse.data().isEmpty()) {
            throw new NoAvailableDriverException(ride.getPickupLocation());
        }

        // Select the first eligible driver from the list
        DriverResponse driver = apiResponse.data().get(0);

        // Update the selected driver's availability status to BUSY
        driverServiceClient.updateAvailability(driver.driverId(), DRIVER_BUSY);

        // Assign driver to the ride and update ride status
        ride.setDriverId(driver.driverId());
        ride.setStatus(RideStatus.ASSIGNED);
        ride.setUpdatedAt(LocalDateTime.now());
        
        return rideRepository.save(ride);
    }

    @Override
    public Ride updateRideStatus(String rideId, RideStatus nextStatus) {
        Ride ride = getRideById(rideId);
        if (!ride.getStatus().isValidNextState(nextStatus)) {
            throw new InvalidStateTransitionException(ride.getStatus(), nextStatus);
        }

        if ((nextStatus == RideStatus.COMPLETED || nextStatus == RideStatus.CANCELLED)
                && ride.getDriverId() != null) {
            // Restore driver status to AVAILABLE upon completion or cancellation
            driverServiceClient.updateAvailability(ride.getDriverId(), DRIVER_AVAILABLE);
        }

        ride.setStatus(nextStatus);
        ride.setUpdatedAt(LocalDateTime.now());
        return rideRepository.save(ride);
    }

    @Override
    public Ride getRideById(String rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RideNotFoundException(rideId));
    }
}