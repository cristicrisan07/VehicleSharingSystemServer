package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Driver;
import com.example.vehiclesharingsystemserver.model.RentalSession;
import com.example.vehiclesharingsystemserver.model.Vehicle;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface RentalSessionRepository extends CrudRepository<RentalSession, UUID> {
    Optional<RentalSession> findRentalSessionByVehicle(Vehicle vehicle);
    Optional<RentalSession> findRentalSessionByVehicleAndAndEndTime(Vehicle vehicle, Timestamp endTime);
    Optional<RentalSession> findRentalSessionByDriver(Driver driver);
}
