package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Account;
import com.example.vehiclesharingsystemserver.model.Administrator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdministratorRepository extends CrudRepository<Administrator,UUID> {
    Optional<Administrator> findByAccount(Account account);
}
