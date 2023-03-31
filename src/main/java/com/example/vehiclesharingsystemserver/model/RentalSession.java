package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "rental_sessions")
public class RentalSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Driver driver;
    @ManyToOne
    private Vehicle vehicle;
    @Column(nullable = false)
    private Timestamp startTime;
    @Column(nullable = false)
    private Timestamp endTime;
    @Column(nullable = false)
    private double cost;
    @OneToOne
    @JoinColumn(name = "payment",referencedColumnName = "id")
    private Payment payment;

    public RentalSession(Driver driver, Vehicle vehicle, Timestamp startTime, Timestamp endTime, double cost, Payment payment) {
        this.driver = driver;
        this.vehicle = vehicle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = cost;
        this.payment = payment;
    }

    public RentalSession() {

    }
}
