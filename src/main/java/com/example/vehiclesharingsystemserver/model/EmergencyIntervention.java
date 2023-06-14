package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emergency_interventions")
public class EmergencyIntervention {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Vehicle vehicle;
    @Column(nullable = false)
    private LocalDateTime issue_time;
    @Column
    private LocalDateTime solution_time;
    @ManyToOne
    private RentalCompanyManager rentalCompanyManager;
    @Column(nullable = false)
    private String action;
    @Column(nullable = false)
    private String reason;
    @Column(nullable = false)
    private String status;

    public EmergencyIntervention(Vehicle vehicle, LocalDateTime issue_time, RentalCompanyManager rentalCompanyManager, String action, String reason, String status) {
        this.vehicle = vehicle;
        this.issue_time = issue_time;
        this.rentalCompanyManager = rentalCompanyManager;
        this.action = action;
        this.reason = reason;
        this.status = status;
    }

    public EmergencyIntervention() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDateTime getIssue_time() {
        return issue_time;
    }

    public void setIssue_time(LocalDateTime issue_time) {
        this.issue_time = issue_time;
    }

    public RentalCompanyManager getRentalCompanyManager() {
        return rentalCompanyManager;
    }

    public void setRentalCompanyManager(RentalCompanyManager rentalCompanyManager) {
        this.rentalCompanyManager = rentalCompanyManager;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getSolution_time() {
        return solution_time;
    }

    public void setSolution_time(LocalDateTime solution_time) {
        this.solution_time = solution_time;
    }
}
