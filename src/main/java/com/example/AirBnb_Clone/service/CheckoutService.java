package com.example.AirBnb_Clone.service;

import com.example.AirBnb_Clone.entity.Booking;

public interface CheckoutService {
    String getCheckoutUrl(Booking bookingId, String successUrl, String failureUrl);
}
