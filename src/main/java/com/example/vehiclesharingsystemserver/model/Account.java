package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true,nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false,unique = true)
    private String phoneNumber;
    @Column(nullable = false,unique = true)
    private String emailAddress;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account() {

    }
}
