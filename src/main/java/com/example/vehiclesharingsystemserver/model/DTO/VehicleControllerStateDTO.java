package com.example.vehiclesharingsystemserver.model.DTO;

public class VehicleControllerStateDTO {
    private final String token;
    private final EmergencyActionDTOForController emergency;

    public VehicleControllerStateDTO(String token, EmergencyActionDTOForController emergency) {
        this.token = token;
        this.emergency = emergency;
    }

    public String getToken() {
        return token;
    }

    public EmergencyActionDTOForController getEmergency() {
        return emergency;
    }
}
