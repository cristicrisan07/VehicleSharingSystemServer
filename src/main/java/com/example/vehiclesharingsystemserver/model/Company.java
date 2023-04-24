package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,unique = true)
    private String name;
    @Column(nullable = false,unique = true)
    private String emailAddress;
    @Column(nullable = false,unique = true)
    private String phoneNumber;
    @OneToMany(mappedBy = "rentalCompany")
    private Set<Vehicle> vehicles;
    @OneToMany(mappedBy = "company")
    private Set<RentalCompanyManager> managers;

    public Company() {

    }

    public Company(String name, String emailAddress, String phoneNumber) {
        this.name = name;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Set<RentalCompanyManager> getManagers() {
        return managers;
    }

    public void setManagers(Set<RentalCompanyManager> managers) {
        this.managers = managers;
    }
}
