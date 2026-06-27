package com.example.TripHive_Backend.strategy;

import com.example.TripHive_Backend.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("basePricingStrategy")
public class BasePricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
