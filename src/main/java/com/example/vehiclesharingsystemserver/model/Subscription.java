package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    public UUID getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String name;
    @ManyToOne
    private RentalPrice rentalPrice;
    @Column(nullable = false)
    private int kilometersLimit;

    @OneToMany(mappedBy = "subscription")
    private Set<ActiveSubscription> activeSubscription;

    public Subscription(String name, RentalPrice rentalPrice, int kilometersLimit) {
        this.name = name;
        this.rentalPrice = rentalPrice;
        this.kilometersLimit = kilometersLimit;
    }

    public Subscription() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RentalPrice getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(RentalPrice rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public int getKilometersLimit() {
        return kilometersLimit;
    }

    public void setKilometersLimit(int kilometersLimit) {
        this.kilometersLimit = kilometersLimit;
    }
}
