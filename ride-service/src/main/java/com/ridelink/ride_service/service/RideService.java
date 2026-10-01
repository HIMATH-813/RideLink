package com.ridelink.ride_service.service;

import com.ridelink.ride_service.dto.CreateRideRequest;
import com.ridelink.ride_service.model.Ride;
import com.ridelink.ride_service.model.RideStatus;

public interface RideService {
    Ride createRide(CreateRideRequest request);

    Ride assignDriver(String rideId);

    Ride updateRideStatus(String rideId, RideStatus nextStatus);

    Ride getRideById(String rideId);
}
