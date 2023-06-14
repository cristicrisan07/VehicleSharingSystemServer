package com.example.vehiclesharingsystemserver.model.DTO;

public class EmergencyActionDTOForController {
    private final String action;
    private final String reason;
    private final String time;

    public EmergencyActionDTOForController(String action, String reason, String time) {
        this.action = action;
        this.reason = reason;
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public String getReason() {
        return reason;
    }

    public String getTime() {
        return time;
    }
}
