package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.DocumentPhoto;
import com.example.vehiclesharingsystemserver.model.IdentityValidationDocument;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface DocumentPhotoRepository extends CrudRepository<DocumentPhoto, UUID> {

    Iterable<DocumentPhoto> findByIdentityValidationDocument(IdentityValidationDocument identityValidationDocument);
}
