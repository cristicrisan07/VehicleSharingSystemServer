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
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "rentalCompany")
    private Set<Vehicle> vehicles;
}
