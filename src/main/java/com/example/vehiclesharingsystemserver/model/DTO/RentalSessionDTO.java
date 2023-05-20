package com.example.vehiclesharingsystemserver.model.DTO;

import org.json.simple.JSONObject;

public class RentalSessionDTO {
    private final String driverUsername;
    private final String vehicleVIN;
    private final String startTime;
    private final String endTime;
    private final String location;
    private final String cost;
    public RentalSessionDTO(String driverUsername, String vehicleVIN, String startTime, String endTime, String location, String cost) {
        this.driverUsername = driverUsername;
        this.vehicleVIN = vehicleVIN;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
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

    public String getCost() {
        return cost;
    }

    public String getLocation() {
        return location;
    }
}
