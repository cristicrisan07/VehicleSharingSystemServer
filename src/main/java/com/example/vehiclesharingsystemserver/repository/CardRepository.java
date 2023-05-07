package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardRepository extends CrudRepository<Card, UUID> {
    Optional<Card> findCardByCardNumber(String cardNumber);
}
