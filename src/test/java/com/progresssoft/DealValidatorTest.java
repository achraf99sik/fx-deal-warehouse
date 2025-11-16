package com.progresssoft;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DealValidatorTest {

    @Test
    void testValidDeal() {
        final Deal validDeal = new Deal(
            UUID.randomUUID(),
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("100.00")
        );
        assertDoesNotThrow(() -> DealValidator.validate(validDeal));
    }

    @Test
    void testNullDeal() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> DealValidator.validate(null)
        );
        assertEquals("Deal cannot be null.", thrown.getMessage());
    }

    @Test
    void testNullDealId() {
        final Deal deal = new Deal(
            null,
            "USD",
            "EUR",
            LocalDateTime.now(),
            new BigDecimal("100.00")
        );
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> DealValidator.validate(deal)
        );
        assertEquals("Deal ID cannot be null.", thrown.getMessage());
    }

    @Test
    void testInvalidFromCurrencyIsoCode() {
        final String[] invalidCodes = {"US", "usd", null};
        for (String code : invalidCodes) {
            final Deal deal = new Deal(UUID.randomUUID(), code, "EUR", LocalDateTime.now(), new BigDecimal("100.00"));
            final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> DealValidator.validate(deal)
            );
            assertEquals("From Currency ISO Code is invalid. Must be a 3-letter uppercase code.", thrown.getMessage());
        }
    }

    @Test
    void testInvalidToCurrencyIsoCode() {
        final String[] invalidCodes = {"EU", "eur", null};
        for (String code : invalidCodes) {
            final Deal deal = new Deal(UUID.randomUUID(), "USD", code, LocalDateTime.now(), new BigDecimal("100.00"));
            final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> DealValidator.validate(deal)
            );
            assertEquals("To Currency ISO Code is invalid. Must be a 3-letter uppercase code.", thrown.getMessage());
        }
    }

    @Test
    void testNullDealTimestamp() {
        final Deal deal = new Deal(
            UUID.randomUUID(),
            "USD",
            "EUR",
            null,
            new BigDecimal("100.00")
        );
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> DealValidator.validate(deal)
        );
        assertEquals("Deal timestamp cannot be null.", thrown.getMessage());
    }

    @Test
    void testInvalidDealAmount() {
        final BigDecimal[] invalidAmounts = {null, new BigDecimal("-10.00"), BigDecimal.ZERO};
        for (BigDecimal amount : invalidAmounts) {
            final Deal deal = new Deal(UUID.randomUUID(), "USD", "EUR", LocalDateTime.now(), amount);
            final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> DealValidator.validate(deal)
            );
            assertEquals("Deal amount cannot be null and must be positive.", thrown.getMessage());
        }
    }

    @Test
    void testSameFromAndToCurrency() {
        final Deal deal = new Deal(UUID.randomUUID(), "USD", "USD", LocalDateTime.now(), new BigDecimal("100.00"));
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> DealValidator.validate(deal)
        );
        assertEquals("From Currency and To Currency cannot be the same.", thrown.getMessage());
    }
}

