package com.example.vehiclesharingsystemserver.model.DTO;

public class RentalPriceDTO {
    private final String value;
    private final String currency;
    private final String timeUnit;

    public RentalPriceDTO(String value, String currency, String timeUnit) {
        this.value = value;
        this.currency = currency;
        this.timeUnit = timeUnit;
    }

    public String getValue() {
        return value;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTimeUnit() {
        return timeUnit;
    }
}
