package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "active_subscriptions")
public class ActiveSubscription extends Payment{
    @ManyToOne
    private Subscription subscription;
    @Column(nullable = false)
    private Timestamp startDate;

    public ActiveSubscription(Subscription subscription, Timestamp startDate) {
        this.subscription = subscription;
        this.startDate = startDate;
    }

    public ActiveSubscription() {

    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }
}
