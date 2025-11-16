package com.progresssoft;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Deal {
    private UUID dealId;
    private String fromCurrencyIsoCode;
    private String toCurrencyIsoCode;
    private LocalDateTime dealTimestamp;
    private BigDecimal dealAmount;

    public Deal(UUID dealId, String fromCurrencyIsoCode, String toCurrencyIsoCode, LocalDateTime dealTimestamp, BigDecimal dealAmount) {
        this.dealId = dealId;
        this.fromCurrencyIsoCode = fromCurrencyIsoCode;
        this.toCurrencyIsoCode = toCurrencyIsoCode;
        this.dealTimestamp = dealTimestamp;
        this.dealAmount = dealAmount;
    }

    public UUID getDealId() {
        return dealId;
    }

    public String getFromCurrencyIsoCode() {
        return fromCurrencyIsoCode;
    }

    public String getToCurrencyIsoCode() {
        return toCurrencyIsoCode;
    }

    public LocalDateTime getDealTimestamp() {
        return dealTimestamp;
    }

    public BigDecimal getDealAmount() {
        return dealAmount;
    }

    public void setDealId(UUID dealId) {
        this.dealId = dealId;
    }

    public void setFromCurrencyIsoCode(String fromCurrencyIsoCode) {
        this.fromCurrencyIsoCode = fromCurrencyIsoCode;
    }

    public void setToCurrencyIsoCode(String toCurrencyIsoCode) {
        this.toCurrencyIsoCode = toCurrencyIsoCode;
    }

    public void setDealTimestamp(LocalDateTime dealTimestamp) {
        this.dealTimestamp = dealTimestamp;
    }

    public void setDealAmount(BigDecimal dealAmount) {
        this.dealAmount = dealAmount;
    }

    @Override
    public String toString() {
        return "Deal{" +
               "dealId=" + dealId +
               ", fromCurrencyIsoCode='" + fromCurrencyIsoCode + "'" +
               ", toCurrencyIsoCode='" + toCurrencyIsoCode + "'" +
               ", dealTimestamp=" + dealTimestamp +
               ", dealAmount=" + dealAmount +
               "}";
    }
}
