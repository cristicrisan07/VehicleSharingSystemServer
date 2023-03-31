package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "drivers")
public class Driver extends User {

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
}
