package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int duration;

    @Column(nullable = false)
    private double price;

    @OneToMany(mappedBy = "subscription")
    private Set<ActiveSubscription> activeSubscription;

    public Subscription(int duration, double price) {
        this.duration = duration;
        this.price = price;
    }

    public Subscription(){

    }
}
