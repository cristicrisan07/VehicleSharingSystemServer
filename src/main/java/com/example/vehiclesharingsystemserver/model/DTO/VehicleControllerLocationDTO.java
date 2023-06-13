package com.example.vehiclesharingsystemserver.model.DTO;

public class VehicleControllerLocationDTO {

    private final String vin;
    private final String location;
    private final String time;

    public VehicleControllerLocationDTO(String vin, String location, String time) {
        this.vin = vin;
        this.location = location;
        this.time = time;
    }

    public String getVin() {
        return vin;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }
}
