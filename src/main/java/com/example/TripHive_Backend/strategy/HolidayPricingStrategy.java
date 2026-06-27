package com.example.TripHive_Backend.strategy;

import com.example.TripHive_Backend.entity.Inventory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("holidayPricingStrategy")
public class HolidayPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;

    public HolidayPricingStrategy(@Qualifier("urgencyPricingStrategy") PricingStrategy wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        boolean isTodayHoliday = true; // This should be determined based on the date and location
        if (isTodayHoliday) {
            price = price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }
}