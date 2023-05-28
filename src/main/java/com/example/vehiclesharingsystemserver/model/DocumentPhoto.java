package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.io.Serializable;
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

    public DocumentPhoto(Blob photo,IdentityValidationDocument identityValidationDocument) {
        this.photo = photo;
        this.identityValidationDocument = identityValidationDocument;
    }

    public DocumentPhoto() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Blob getPhoto() {
        return photo;
    }

    public void setPhoto(Blob photo) {
        this.photo = photo;
    }

    public IdentityValidationDocument getIdentityValidationDocument() {
        return identityValidationDocument;
    }

    public void setIdentityValidationDocument(IdentityValidationDocument identityValidationDocument) {
        this.identityValidationDocument = identityValidationDocument;
    }
}
