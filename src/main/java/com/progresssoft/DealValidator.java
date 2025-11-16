package com.progresssoft;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class DealValidator {

    private static final Pattern CURRENCY_CODE_PATTERN = Pattern.compile("^[A-Z]{3}$");

    public static void validate(Deal deal) throws IllegalArgumentException {
        if (deal == null) {
            throw new IllegalArgumentException("Deal cannot be null.");
        }
        if (deal.getDealId() == null) {
            throw new IllegalArgumentException("Deal ID cannot be null.");
        }
        if (deal.getFromCurrencyIsoCode() == null || !CURRENCY_CODE_PATTERN.matcher(deal.getFromCurrencyIsoCode()).matches()) {
            throw new IllegalArgumentException("From Currency ISO Code is invalid. Must be a 3-letter uppercase code.");
        }
        if (deal.getToCurrencyIsoCode() == null || !CURRENCY_CODE_PATTERN.matcher(deal.getToCurrencyIsoCode()).matches()) {
            throw new IllegalArgumentException("To Currency ISO Code is invalid. Must be a 3-letter uppercase code.");
        }
        if (deal.getDealTimestamp() == null) {
            throw new IllegalArgumentException("Deal timestamp cannot be null.");
        }
        if (deal.getDealAmount() == null || deal.getDealAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deal amount cannot be null and must be positive.");
        }
        if (deal.getFromCurrencyIsoCode().equals(deal.getToCurrencyIsoCode())) {
            throw new IllegalArgumentException("From Currency and To Currency cannot be the same.");
        }
    }
}
