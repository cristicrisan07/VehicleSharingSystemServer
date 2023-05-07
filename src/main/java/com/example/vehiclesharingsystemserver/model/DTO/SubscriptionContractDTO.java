package com.example.vehiclesharingsystemserver.model.DTO;

public class SubscriptionContractDTO {
    private final String driverUsername;
    private final String subscriptionId;
    private final String encryptedCardNumber;
    private final String value;

    public SubscriptionContractDTO(String driverUsername, String subscriptionId, String encryptedCardNumber, String value) {
        this.driverUsername = driverUsername;
        this.subscriptionId = subscriptionId;
        this.encryptedCardNumber = encryptedCardNumber;
        this.value = value;
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

    public String getValue() {
        return value;
    }
}
