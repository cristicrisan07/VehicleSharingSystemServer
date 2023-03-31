package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.sql.Blob;
import java.util.UUID;

@Entity
@Table(name = "documents_photos")
public class DocumentPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    @Lob
    private Blob photo;
    @ManyToOne
    private IdentityValidationDocument identityValidationDocument;

    public DocumentPhoto(Blob photo) {
        this.photo = photo;
    }

    public DocumentPhoto() {
    }
}
