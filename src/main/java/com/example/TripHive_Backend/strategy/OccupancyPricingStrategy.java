package com.example.TripHive_Backend.strategy;

import com.example.TripHive_Backend.entity.Inventory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("occupancyPricingStrategy")
public class OccupancyPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;

    public OccupancyPricingStrategy(@Qualifier("surgePricingStrategy") PricingStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        double occupancyRate = (double) inventory.getBookedCount() / inventory.getTotalCount();
        if (occupancyRate > 0.8) {
            price = price.multiply(BigDecimal.valueOf(1.2));
        }
        return price;
    }
}
