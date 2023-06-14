package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "rental_company_managers")
public class RentalCompanyManager extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Company company;
    @OneToMany(mappedBy = "rentalCompanyManager")
    private Set<EmergencyIntervention> emergencyInterventions;

    public RentalCompanyManager(String firstName, String lastName, Account account,Company company) {
        super(firstName, lastName, account);
        this.company=company;
    }


    public RentalCompanyManager(){

    }
    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
