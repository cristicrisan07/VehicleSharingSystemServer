package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.ActiveSubscription;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;



public interface ActiveSubscriptionRepository extends CrudRepository<ActiveSubscription, UUID> {
}
