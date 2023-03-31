package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "cards")
public class Card extends Payment{
    @Column(nullable = false,unique = true)
    private String cardNumber;

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Card() {

    }
}
