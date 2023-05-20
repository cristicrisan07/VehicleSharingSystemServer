package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Driver;
import com.example.vehiclesharingsystemserver.model.Payment;
import com.example.vehiclesharingsystemserver.model.RentalSession;
import com.example.vehiclesharingsystemserver.model.Vehicle;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface RentalSessionRepository extends CrudRepository<RentalSession, UUID> {
    Optional<RentalSession> findRentalSessionByVehicle(Vehicle vehicle);
    Optional<RentalSession> findRentalSessionByVehicleAndEndTime(Vehicle vehicle, LocalDateTime endTime);
    Optional<RentalSession> findRentalSessionByDriverAndEndTime(Driver driver, LocalDateTime endTime);
    Optional<RentalSession> findRentalSessionByDriverAndPayment(Driver driver, Payment payment);
    Optional<RentalSession> findRentalSessionByDriver(Driver driver);
}
