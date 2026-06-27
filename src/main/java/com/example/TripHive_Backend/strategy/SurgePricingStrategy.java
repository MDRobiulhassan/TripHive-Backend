package com.example.TripHive_Backend.strategy;

import com.example.TripHive_Backend.entity.Inventory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("surgePricingStrategy")
public class SurgePricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;

    public SurgePricingStrategy(@Qualifier("basePricingStrategy") PricingStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        return price.multiply(inventory.getSurgeFactor());
    }
}
