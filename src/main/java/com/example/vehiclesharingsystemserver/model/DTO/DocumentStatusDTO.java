package com.example.vehiclesharingsystemserver.model.DTO;

public class DocumentStatusDTO {
    private final String driverUsername;
    private final String validationStatus;
    public DocumentStatusDTO(String driverUsername, String validationStatus) {
        this.driverUsername = driverUsername;
        this.validationStatus = validationStatus;
    }

    public String getDriverUsername() {
        return driverUsername;
    }

    public String getValidationStatus() {
        return validationStatus;
    }
}
