package com.example.AirBnb_Clone.strategy;

import com.example.AirBnb_Clone.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}