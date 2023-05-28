package com.example.vehiclesharingsystemserver.model.DTO;

public class IdentityValidationDocumentDTO {
    private final String username;
    private final String photoFront;
    private final String photoBack;

    public IdentityValidationDocumentDTO(String username, String photoFront, String photoBack) {
        this.username = username;
        this.photoFront = photoFront;
        this.photoBack = photoBack;
    }

    public String getUsername() {
        return username;
    }

    public String getPhotoFront() {
        return photoFront;
    }

    public String getPhotoBack() {
        return photoBack;
    }
}
