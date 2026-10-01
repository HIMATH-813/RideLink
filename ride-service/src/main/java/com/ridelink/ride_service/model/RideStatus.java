package com.ridelink.ride_service.model;

public enum RideStatus {
    REQUESTED,
    ASSIGNED,
    ACCEPTED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public boolean isValidNextState(RideStatus nextState) {
        if (nextState == null) {
            return false;
        }

        return switch (this) {
            case REQUESTED -> nextState == ASSIGNED || nextState == ACCEPTED || nextState == CANCELLED;
            case ASSIGNED -> nextState == ACCEPTED || nextState == CANCELLED;
            case ACCEPTED -> nextState == IN_PROGRESS || nextState == CANCELLED;
            case IN_PROGRESS -> nextState == COMPLETED || nextState == CANCELLED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}
