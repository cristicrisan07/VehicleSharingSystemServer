package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
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
    private LocalDateTime startTime;
    @Column
    private LocalDateTime endTime;
    @Column(nullable = false)
    private double cost;
    @OneToOne
    @JoinColumn(name = "payment",referencedColumnName = "id")
    private Payment payment;

    public RentalSession(Driver driver, Vehicle vehicle, LocalDateTime startTime) {
        this.driver = driver;
        this.vehicle = vehicle;
        this.startTime = startTime;
    }

    public RentalSession() {

    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public UUID getId() {
        return id;
    }
}
