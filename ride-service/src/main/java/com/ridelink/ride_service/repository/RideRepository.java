package com.ridelink.ride_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ridelink.ride_service.model.Ride;

public interface RideRepository extends MongoRepository<Ride, String> {
}
