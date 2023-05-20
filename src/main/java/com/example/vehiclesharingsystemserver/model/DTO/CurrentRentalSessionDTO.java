package com.example.vehiclesharingsystemserver.model.DTO;



public class CurrentRentalSessionDTO {
    private final String id;
    private final VehicleDTO vehicle;
    private final String startTime;
    private final String endTime;
    private final Double cost;

    public CurrentRentalSessionDTO(String id, VehicleDTO vehicle, String startTime, String endTime, Double cost) {
        this.id = id;
        this.vehicle = vehicle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
    }

    public VehicleDTO getVehicle() {
        return vehicle;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Double getCost() {
        return cost;
    }

    public String getId() {
        return id;
    }
}
