package com.example.vehiclesharingsystemserver.repository;


import com.example.vehiclesharingsystemserver.model.Company;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends CrudRepository<Company, UUID> {

    Optional<Company> findCompaniesByName(String name);
    Optional<Company> findCompaniesByEmailAddress(String emailAddress);
    Optional<Company> findCompaniesByPhoneNumber(String phoneNumber);
}
