package com.example.vehiclesharingsystemserver.model.DTO;

public class SubscriptionContractDTO {
    private final String driverUsername;
    private final String subscriptionId;
    private final  String encryptedCardNumber;

    public SubscriptionContractDTO(String driverUsername, String subscriptionId, String encryptedCardNumber) {
        this.driverUsername = driverUsername;
        this.subscriptionId = subscriptionId;
        this.encryptedCardNumber = encryptedCardNumber;
    }

    public String getDriverUsername() {
        return driverUsername;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }
}
