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


    public IdentityValidationDocument(String type, String status) {
        this.type = type;
        this.status = status;
    }

    public IdentityValidationDocument(){

    }
}
