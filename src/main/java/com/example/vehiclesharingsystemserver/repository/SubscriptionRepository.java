package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends CrudRepository<Subscription, UUID> {
    Optional<Subscription> findSubscriptionByName(String name);
}
