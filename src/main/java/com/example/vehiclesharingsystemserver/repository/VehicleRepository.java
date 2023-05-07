package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Company;
import com.example.vehiclesharingsystemserver.model.Vehicle;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends CrudRepository<Vehicle, UUID> {
    Optional<Vehicle> findVehicleByVin(String vin);
    Optional<Vehicle> findVehicleByRegistrationNumber(String registrationNumber);
    Iterable<Vehicle> findAllByRentalCompany(Company company);
}
