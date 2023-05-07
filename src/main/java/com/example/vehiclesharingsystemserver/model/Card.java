package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "cards")
public class Card{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false,unique = true)
    private String cardNumber;

    public Card(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Card() {

    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
