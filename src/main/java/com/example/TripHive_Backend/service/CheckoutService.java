package com.example.TripHive_Backend.service;

import com.example.TripHive_Backend.entity.Booking;

public interface CheckoutService {
    String getCheckoutUrl(Booking bookingId, String successUrl, String failureUrl);
}
