package com.ridelink.farepayment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.farepayment.dto.FareEstimateRequest;
import com.ridelink.farepayment.model.Payment;
import com.ridelink.farepayment.service.FarePaymentService;

@RestController
@RequestMapping("/api/fare-payment")
public class FarePaymentController {

    @Autowired
    private FarePaymentService service;

    @PostMapping("/estimate")
    public ResponseEntity<Double> estimateFare(@RequestBody FareEstimateRequest request) {
        double fare = service.calculateFare(request.getDistanceKm(), request.getEstimatedTimeMinutes());
        return ResponseEntity.ok(fare);
    }

    @PostMapping("/pay")
    public ResponseEntity<Payment> processPayment(@RequestParam String rideId, @RequestParam double amount) {
        Payment payment = service.processPayment(rideId, amount);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/receipt/{rideId}")
    public ResponseEntity<Payment> getReceipt(@PathVariable String rideId) {
        Payment receipt = service.getReceiptByRideId(rideId);
        return ResponseEntity.ok(receipt);
    }
}