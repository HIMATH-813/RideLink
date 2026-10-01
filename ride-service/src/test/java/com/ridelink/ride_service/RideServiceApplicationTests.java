package com.ridelink.ride_service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ridelink.ride_service.model.RideStatus;

@SpringBootTest(properties = "spring.mongodb.uri=mongodb://localhost/test")
class RideServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void allowsOnlyTheNextLifecycleState() {
		assertTrue(RideStatus.REQUESTED.isValidNextState(RideStatus.ASSIGNED));
		assertTrue(RideStatus.REQUESTED.isValidNextState(RideStatus.ACCEPTED));
		assertTrue(RideStatus.ASSIGNED.isValidNextState(RideStatus.ACCEPTED));
		assertTrue(RideStatus.ACCEPTED.isValidNextState(RideStatus.IN_PROGRESS));
		assertTrue(RideStatus.IN_PROGRESS.isValidNextState(RideStatus.COMPLETED));
		assertFalse(RideStatus.IN_PROGRESS.isValidNextState(RideStatus.ASSIGNED));
	}

	@Test
	void allowsCancellationOnlyBeforeTerminalStates() {
		assertTrue(RideStatus.REQUESTED.isValidNextState(RideStatus.CANCELLED));
		assertTrue(RideStatus.ASSIGNED.isValidNextState(RideStatus.CANCELLED));
		assertTrue(RideStatus.ACCEPTED.isValidNextState(RideStatus.CANCELLED));
		assertTrue(RideStatus.IN_PROGRESS.isValidNextState(RideStatus.CANCELLED));
		for (RideStatus nextState : RideStatus.values()) {
			assertFalse(RideStatus.COMPLETED.isValidNextState(nextState));
			assertFalse(RideStatus.CANCELLED.isValidNextState(nextState));
		}
		assertFalse(RideStatus.REQUESTED.isValidNextState(null));
	}

}
