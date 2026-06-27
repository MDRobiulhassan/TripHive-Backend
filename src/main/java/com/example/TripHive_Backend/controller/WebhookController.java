package com.example.TripHive_Backend.controller;

import com.example.TripHive_Backend.service.BookingService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhook")
public class WebhookController {

    private final BookingService bookingService;

    @Value("${stripe.webhook-secret}")
    private String endpointSecret;

    @PostMapping("/payment")
    public ResponseEntity<?> capturePayment(
            @RequestBody String payload,
            @RequestHeader("stripe-signature") String sigHeader
    ) {

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            bookingService.capturePayment(event);

            return ResponseEntity.noContent().build();

        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(400).body("Invalid signature");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Webhook error: " + e.getMessage());
        }
    }
}