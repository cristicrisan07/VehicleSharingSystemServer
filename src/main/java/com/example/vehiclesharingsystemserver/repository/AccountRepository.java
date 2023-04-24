package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmailAddress(String emailAddress);
    Optional<Account> findAccountByPhoneNumber(String phoneNumber);
 }
