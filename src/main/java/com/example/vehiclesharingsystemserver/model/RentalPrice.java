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
    private TimeUnit timeUnit;
    @OneToMany(mappedBy = "rentalPrice")
    private Set<Vehicle> vehicles;

    public RentalPrice(double value, Currency currency, TimeUnit timeUnit) {
        this.value = value;
        this.currency = currency;
        this.timeUnit = timeUnit;
    }

    public RentalPrice() {

    }
}
