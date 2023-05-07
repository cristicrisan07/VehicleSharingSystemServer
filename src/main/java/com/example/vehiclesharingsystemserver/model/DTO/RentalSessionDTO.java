package com.example.vehiclesharingsystemserver.model.DTO;

public class RentalSessionDTO {
    private final String driverUsername;
    private final String vehicleVIN;
    private final String startTime;
    private final String endTime;
    private final String encryptedCardNumber;
    private final String cost;
    public RentalSessionDTO(String driverUsername, String vehicleVIN, String startTime, String endTime, String encryptedCardNumber, String cost) {
        this.driverUsername = driverUsername;
        this.vehicleVIN = vehicleVIN;
        this.startTime = startTime;
        this.endTime = endTime;
        this.encryptedCardNumber = encryptedCardNumber;
        this.cost = cost;
    }

    public String getDriverUsername() {
        return driverUsername;
    }

    public String getVehicleVIN() {
        return vehicleVIN;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getEncryptedCardNumber() {
        return encryptedCardNumber;
    }

    public String getCost() {
        return cost;
    }
}
