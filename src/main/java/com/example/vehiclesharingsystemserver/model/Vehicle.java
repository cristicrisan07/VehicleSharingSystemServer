package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.sql.Blob;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,unique = true)
    private String vin;
    @Column(nullable = false)
    private String manufacturer;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private int rangeLeftInKm;
    @Column(nullable = false)
    private int yearOfManufacture;
    @Column(nullable = false)
    private int horsePower;
    @Column(nullable = false)
    private int torque;
    @Column(nullable = false)
    private int maximumAuthorisedMassInKg;
    @Column(nullable = false)
    private int numberOfSeats;
    @Column(nullable = false)
    private String location;
    @ManyToOne
    private RentalPrice rentalPrice;
    @Column(nullable = false)
    @Lob
    private Blob image;
    @OneToMany(mappedBy = "vehicle")
    private Set<RentalSession> rentalSessions;
    @ManyToOne
    private Company rentalCompany;


}
