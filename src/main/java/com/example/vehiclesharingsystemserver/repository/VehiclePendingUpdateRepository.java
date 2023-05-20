package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.VehiclePendingUpdate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface VehiclePendingUpdateRepository extends CrudRepository<VehiclePendingUpdate, UUID> {

    Optional<VehiclePendingUpdate> findVehiclePendingUpdateByVin(String vin);
}
