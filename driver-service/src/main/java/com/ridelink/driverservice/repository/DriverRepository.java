package com.ridelink.driverservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ridelink.driverservice.model.AvailabilityStatus;
import com.ridelink.driverservice.model.Driver;

@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {
    Optional<Driver> findByDriverId(String driverId);
    Optional<Driver> findByUserId(String userId);
    List<Driver> findByAvailabilityStatusAndServiceArea(AvailabilityStatus availabilityStatus, String serviceArea);
    List<Driver> findByAvailabilityStatusAndServiceAreaAndLocationIsNotNull(AvailabilityStatus availabilityStatus, String serviceArea);
}
