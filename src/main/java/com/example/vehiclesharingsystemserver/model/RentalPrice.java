package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.Currency;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "rental_prices")
public class RentalPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private double value;
    @Column(nullable = false)
    private Currency currency;
    @Column(nullable = false)
    private String timeUnit;
    @OneToMany(mappedBy = "rentalPrice")
    private Set<Vehicle> vehicles;

    public RentalPrice(double value, Currency currency, String timeUnit) {
        this.value = value;
        this.currency = currency;
        this.timeUnit = timeUnit;
    }

    public RentalPrice() {

    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
