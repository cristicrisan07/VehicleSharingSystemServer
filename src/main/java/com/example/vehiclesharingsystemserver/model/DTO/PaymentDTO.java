package com.example.vehiclesharingsystemserver.model.DTO;

public class PaymentDTO {
    private final String encryptedCardNumber;
    private final String cost;
    private final String rentalSessionId;

    public PaymentDTO(String encryptedCardNumber, String cost, String rentalSessionId) {
        this.encryptedCardNumber = encryptedCardNumber;
        this.cost = cost;
        this.rentalSessionId = rentalSessionId;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public String getCost() {
        return cost;
    }

    public String getRentalSessionId() {
        return rentalSessionId;
    }
}
