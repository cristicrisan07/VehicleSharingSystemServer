package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "administrators")
public class Administrator extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    public Administrator(String firstName, String lastName, Account account) {
        super(firstName, lastName, account);
    }

    public Administrator(){

    }
    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
