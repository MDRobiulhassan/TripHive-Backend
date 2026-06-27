package com.example.TripHive_Backend.strategy;

import com.example.TripHive_Backend.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}