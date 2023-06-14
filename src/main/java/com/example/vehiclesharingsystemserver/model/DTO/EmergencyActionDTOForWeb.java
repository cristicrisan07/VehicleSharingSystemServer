package com.example.vehiclesharingsystemserver.model.DTO;

public class EmergencyActionDTOForWeb {
    private final String username;
    private final String vin;
    private final String action;

    public EmergencyActionDTOForWeb(String username, String vin, String action) {
        this.username = username;
        this.vin = vin;
        this.action = action;
    }

    public String getUsername() {
        return username;
    }

    public String getVin() {
        return vin;
    }

    public String getAction() {
        return action;
    }
}
