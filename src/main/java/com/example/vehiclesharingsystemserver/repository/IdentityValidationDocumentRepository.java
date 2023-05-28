package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.Driver;
import com.example.vehiclesharingsystemserver.model.IdentityValidationDocument;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface IdentityValidationDocumentRepository extends CrudRepository<IdentityValidationDocument, UUID> {
        Optional<IdentityValidationDocument> findByDriver(Driver driver);
}
