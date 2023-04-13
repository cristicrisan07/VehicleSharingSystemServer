package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Account;
import com.example.vehiclesharingsystemserver.model.Driver;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository extends CrudRepository<Driver, UUID> {

    Optional<Driver> findByAccount(Account account);
}
