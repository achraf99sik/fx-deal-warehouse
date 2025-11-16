package com.progresssoft;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class MainApplication {
    public static void main(String[] args) {
        System.out.println("Starting FX Deal Warehouse Application...");

        DealRepository dealRepository = new DealRepository();
        DealService dealService = new DealService(dealRepository);

        try {
            // Example 1: Valid new deal
            Deal deal1 = new Deal(UUID.randomUUID(), "USD", "EUR", LocalDateTime.now(), new BigDecimal("100.50"));
            System.out.println("Processing Deal 1: " + deal1);
            dealService.processDeal(deal1);

            // Example 2: Another valid new deal
            Deal deal2 = new Deal(UUID.randomUUID(), "GBP", "JPY", LocalDateTime.now(), new BigDecimal("15000.75"));
            System.out.println("Processing Deal 2: " + deal2);
            dealService.processDeal(deal2);

            // Example 3: Duplicate deal (should be skipped and logged as warning)
            System.out.println("Processing Deal 1 again (duplicate): " + deal1);
            dealService.processDeal(deal1);

            // Example 4: Invalid deal (should be logged as validation error)
            Deal invalidDeal = new Deal(UUID.randomUUID(), "US", "EUR", LocalDateTime.now(), new BigDecimal("50.00"));
            System.out.println("Processing Invalid Deal: " + invalidDeal);
            dealService.processDeal(invalidDeal);

            // Example 5: Deal with same from and to currency (should be logged as validation error)
            Deal sameCurrencyDeal = new Deal(UUID.randomUUID(), "CAD", "CAD", LocalDateTime.now(), new BigDecimal("200.00"));
            System.out.println("Processing Same Currency Deal: " + sameCurrencyDeal);
            dealService.processDeal(sameCurrencyDeal);

        } finally {
            dealRepository.close(); // Close the connection pool
            System.out.println("FX Deal Warehouse Application finished.");
        }
    }
}
