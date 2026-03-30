package com.example.AirBnb_Clone.strategy;

import com.example.AirBnb_Clone.entity.Inventory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("basePricingStrategy")
public class BasePricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
