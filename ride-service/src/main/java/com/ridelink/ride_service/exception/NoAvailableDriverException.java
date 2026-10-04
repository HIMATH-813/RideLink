package com.ridelink.ride_service.exception;

public class NoAvailableDriverException extends RuntimeException {
    public NoAvailableDriverException(String pickupLocation) {
        super("No available driver found for pickup location: " + pickupLocation);
    }
}
