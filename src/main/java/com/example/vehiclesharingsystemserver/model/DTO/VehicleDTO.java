package com.example.vehiclesharingsystemserver.model.DTO;

public class VehicleDTO {
    public final String vin;
    public final String manufacturer;
    public final String model;
    public final String rangeLeftInKm;
    public final String yearOfManufacture;
    public final String horsePower;
    public final String torque;
    public final String maximumAuthorisedMassInKg;
    public final String numberOfSeats;
    public final String location;
    public final RentalPriceDTO rentalPriceDTO;
    public final String rentalCompanyName;

    public VehicleDTO(String vin, String manufacturer, String model, String rangeLeftInKm, String yearOfManufacture, String horsePower, String torque, String maximumAuthorisedMassInKg, String numberOfSeats, String location, RentalPriceDTO rentalPriceDTO, String rentalCompanyName) {
        this.vin = vin;
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
}
