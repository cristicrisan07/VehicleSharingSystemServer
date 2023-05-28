package com.example.vehiclesharingsystemserver.repository;

import com.example.vehiclesharingsystemserver.model.DocumentPhoto;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface DocumentPhotoRepository extends CrudRepository<DocumentPhoto, UUID> {
}
