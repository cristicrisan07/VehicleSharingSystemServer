package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Account;
import com.example.vehiclesharingsystemserver.model.RentalCompanyManager;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface RentalCompanyManagerRepository extends CrudRepository<RentalCompanyManager, UUID> {

    Optional<RentalCompanyManager> findRentalCompanyManagerByAccount(Account account);
}
