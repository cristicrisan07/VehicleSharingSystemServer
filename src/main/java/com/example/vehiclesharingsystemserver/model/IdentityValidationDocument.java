package com.example.vehiclesharingsystemserver.model;

import jakarta.persistence.*;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "validation_documents")
public class IdentityValidationDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToMany(mappedBy = "identityValidationDocument")
    private Set<DocumentPhoto> photos;
    @ManyToOne
    private Driver driver;
    @Column
    private String type;
    @Column
    private String status;


    public IdentityValidationDocument(String type, String status,Driver driver) {
        this.type = type;
        this.status = status;
        this.driver = driver;
    }

    public IdentityValidationDocument(){

    }

    public Set<DocumentPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<DocumentPhoto> photos) {
        this.photos = photos;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
