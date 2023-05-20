package com.example.vehiclesharingsystemserver.model.DTO;

import org.json.simple.JSONObject;

public class VehicleDTO {
    private final String vin;
    private final String registrationNumber;
    private final String manufacturer;
    private final String model;
    private final String rangeLeftInKm;
    private final String yearOfManufacture;
    private final String horsePower;
    private final String torque;
    private final String maximumAuthorisedMassInKg;
    private final String numberOfSeats;
    private final String location;
    private final RentalPriceDTO rentalPriceDTO;
    private final String rentalCompanyName;
    private final boolean available;

    public VehicleDTO(String vin, String registrationNumber, String manufacturer, String model, String rangeLeftInKm, String yearOfManufacture, String horsePower, String torque, String maximumAuthorisedMassInKg, String numberOfSeats, String location, RentalPriceDTO rentalPriceDTO, String rentalCompanyName, boolean available) {
        this.vin = vin;
        this.registrationNumber = registrationNumber;
        this.manufacturer = manufacturer;
        this.model = model;
        this.rangeLeftInKm = rangeLeftInKm;
        this.yearOfManufacture = yearOfManufacture;
        this.horsePower = horsePower;
        this.torque = torque;
        this.maximumAuthorisedMassInKg = maximumAuthorisedMassInKg;
        this.numberOfSeats = numberOfSeats;
        this.location = location;
        this.rentalPriceDTO = rentalPriceDTO;
        this.rentalCompanyName = rentalCompanyName;
        this.available = available;
    }

    public String getVin() {
        return vin;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getRangeLeftInKm() {
        return rangeLeftInKm;
    }

    public String getYearOfManufacture() {
        return yearOfManufacture;
    }

    public String getHorsePower() {
        return horsePower;
    }

    public String getTorque() {
        return torque;
    }

    public String getMaximumAuthorisedMassInKg() {
        return maximumAuthorisedMassInKg;
    }

    public String getNumberOfSeats() {
        return numberOfSeats;
    }

    public String getLocation() {
        return location;
    }

    public RentalPriceDTO getRentalPriceDTO() {
        return rentalPriceDTO;
    }

    public String getRentalCompanyName() {
        return rentalCompanyName;
    }
    public String getRegistrationNumber() {return registrationNumber;}

    public boolean isAvailable() {
        return available;
    }

}
