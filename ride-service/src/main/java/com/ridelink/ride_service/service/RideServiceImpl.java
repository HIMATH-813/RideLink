package com.ridelink.ride_service.service;

import java.time.LocalDateTime;

import feign.FeignException;
import org.springframework.stereotype.Service;

import com.ridelink.ride_service.client.DriverServiceClient;
import com.ridelink.ride_service.dto.CreateRideRequest;
import com.ridelink.ride_service.dto.DriverResponse;
import com.ridelink.ride_service.dto.DriverStatusUpdateRequest;
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
        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidStateTransitionException(ride.getStatus(), RideStatus.ASSIGNED);
        }

        DriverResponse driver;
        try {
            driver = driverServiceClient.findAvailableDriver(ride.getPickupLocation());
        } catch (FeignException.NotFound exception) {
            throw new NoAvailableDriverException(ride.getPickupLocation());
        }
        if (driver == null || driver.id() == null || !driver.available()) {
            throw new NoAvailableDriverException(ride.getPickupLocation());
        }

        driverServiceClient.updateDriverStatus(
                driver.id(), new DriverStatusUpdateRequest(DRIVER_BUSY));
        ride.setDriverId(driver.id());
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
            driverServiceClient.updateDriverStatus(
                    ride.getDriverId(), new DriverStatusUpdateRequest(DRIVER_AVAILABLE));
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
