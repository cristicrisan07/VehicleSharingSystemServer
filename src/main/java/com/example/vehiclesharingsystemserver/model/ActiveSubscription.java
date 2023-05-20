package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "active_subscriptions")
public class ActiveSubscription{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    private Subscription subscription;
    @Column(nullable = false)
    private LocalDateTime startDate;
    @Column(nullable = false)
    private LocalDateTime endDate;
    @OneToOne
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private Payment payment;


    public ActiveSubscription(Subscription subscription, LocalDateTime startDate, LocalDateTime endDate,Payment payment) {
        this.subscription = subscription;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payment = payment;
    }

    public ActiveSubscription() {

    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
