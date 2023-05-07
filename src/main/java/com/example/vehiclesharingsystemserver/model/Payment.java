package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private Card card;

    @OneToOne
    @JoinColumn(name = "active_subscription_id", referencedColumnName = "id")
    private ActiveSubscription activeSubscription;

    public Payment(){

    }
    public Payment(Card card) {
        this.card = card;
    }

    public Payment(ActiveSubscription activeSubscription) {
        this.activeSubscription = activeSubscription;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ActiveSubscription getActiveSubscription() {
        return activeSubscription;
    }

    public void setActiveSubscription(ActiveSubscription activeSubscription) {
        this.activeSubscription = activeSubscription;
    }
}
