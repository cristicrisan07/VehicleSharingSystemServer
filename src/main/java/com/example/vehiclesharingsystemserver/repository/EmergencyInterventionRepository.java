package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.EmergencyIntervention;
import com.example.vehiclesharingsystemserver.model.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

public interface EmergencyInterventionRepository extends CrudRepository<EmergencyIntervention, UUID> {

    void deleteAllByAction(String action);
}
