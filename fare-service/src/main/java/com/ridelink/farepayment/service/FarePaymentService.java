package com.ridelink.farepayment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ridelink.farepayment.exception.ResourceNotFoundException;
import com.ridelink.farepayment.model.Payment;
import com.ridelink.farepayment.repository.PaymentRepository;

@Service
public class FarePaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    // Documented Fare Calculation Rule: Base Fare + (Distance * Rate) + (Time * Rate)
    private static final double BASE_FARE = 50.0;
    private static final double RATE_PER_KM = 30.0;
    private static final double RATE_PER_MIN = 5.0;

    public double calculateFare(double distanceKm, double timeMinutes) {
        return BASE_FARE + (distanceKm * RATE_PER_KM) + (timeMinutes * RATE_PER_MIN);
    }

    public Payment processPayment(String rideId, double amount) {
        String status = (amount > 0) ? "SUCCESS" : "FAILED";
        Payment payment = new Payment(rideId, amount, status);
        return paymentRepository.save(payment);
    }

    public Payment getReceiptByRideId(String rideId) {
        return paymentRepository.findByRideId(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Receipt not found for ride ID: " + rideId));
    }
}