package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "drivers")
public class Driver extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToMany(mappedBy = "driver")
    private Set<IdentityValidationDocument> requiredDocuments;

    @OneToOne
    @JoinColumn(name = "activeSubscriptionId",referencedColumnName = "id")
    private ActiveSubscription activeSubscription;

    @OneToMany(mappedBy = "driver")
    private Set<RentalSession> rentalSessions;

    public Driver(String firstName, String lastName, Account account) {
        super(firstName, lastName, account);
    }

    public Driver() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<IdentityValidationDocument> getRequiredDocuments() {
        return requiredDocuments;
    }

    public void setRequiredDocuments(Set<IdentityValidationDocument> requiredDocuments) {
        this.requiredDocuments = requiredDocuments;
    }

    public ActiveSubscription getActiveSubscription() {
        return activeSubscription;
    }

    public void setActiveSubscription(ActiveSubscription activeSubscription) {
        this.activeSubscription = activeSubscription;
    }

    public Set<RentalSession> getRentalSessions() {
        return rentalSessions;
    }

    public void setRentalSessions(Set<RentalSession> rentalSessions) {
        this.rentalSessions = rentalSessions;
    }

}
