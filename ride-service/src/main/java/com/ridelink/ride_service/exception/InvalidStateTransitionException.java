package com.ridelink.ride_service.exception;

import com.ridelink.ride_service.model.RideStatus;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(RideStatus currentState, RideStatus nextState) {
        super("Invalid ride status transition from " + currentState + " to " + nextState);
    }
}
