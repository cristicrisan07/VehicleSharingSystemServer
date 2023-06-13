package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,unique = true)
    private String vin;
    @Column(nullable = false,unique = true)
    private String registrationNumber;
    @Column(nullable = false)
    private String manufacturer;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private int rangeLeftInKm;
    @Column(nullable = false)
    private int yearOfManufacture;
    @Column(nullable = false)
    private int horsePower;
    @Column(nullable = false)
    private int torque;
    @Column(nullable = false)
    private int maximumAuthorisedMassInKg;
    @Column(nullable = false)
    private int numberOfSeats;
    @Column(nullable = false)
    private String location;
    private LocalDateTime lastLocationReading;
    @ManyToOne
    private RentalPrice rentalPrice;
    @Lob
    private Blob image;
    @OneToMany(mappedBy = "vehicle")
    private Set<RentalSession> rentalSessions;
    @ManyToOne
    private Company rentalCompany;
    @Column(nullable = false)
    private boolean available;

    public Vehicle(String vin, String registrationNumber, String manufacturer, String model, int rangeLeftInKm, int yearOfManufacture,
                   int horsePower, int torque, int maximumAuthorisedMassInKg, int numberOfSeats,
                   String location, RentalPrice rentalPrice, Company rentalCompany,boolean available) {
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
        this.rentalPrice = rentalPrice;
        this.rentalCompany = rentalCompany;
        this.available = available;
    }

    public Vehicle() {

    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getRangeLeftInKm() {
        return rangeLeftInKm;
    }

    public void setRangeLeftInKm(int rangeLeftInKm) {
        this.rangeLeftInKm = rangeLeftInKm;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    public int getTorque() {
        return torque;
    }

    public void setTorque(int torque) {
        this.torque = torque;
    }

    public int getMaximumAuthorisedMassInKg() {
        return maximumAuthorisedMassInKg;
    }

    public void setMaximumAuthorisedMassInKg(int maximumAuthorisedMassInKg) {
        this.maximumAuthorisedMassInKg = maximumAuthorisedMassInKg;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public RentalPrice getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(RentalPrice rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public Set<RentalSession> getRentalSessions() {
        return rentalSessions;
    }

    public void setRentalSessions(Set<RentalSession> rentalSessions) {
        this.rentalSessions = rentalSessions;
    }

    public Company getRentalCompany() {
        return rentalCompany;
    }

    public void setRentalCompany(Company rentalCompany) {
        this.rentalCompany = rentalCompany;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getLastLocationReading() {
        return lastLocationReading;
    }

    public void setLastLocationReading(LocalDateTime lastLocationReading) {
        this.lastLocationReading = lastLocationReading;
    }
}

