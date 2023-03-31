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






}
