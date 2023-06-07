package com.example.vehiclesharingsystemserver.model.DTO;

public class IdentityValidationDocumentDTO {
    private final String username;
    private final String photoFront;
    private final String photoBack;
    private final String photoIDCard;

    public IdentityValidationDocumentDTO(String username, String photoFront, String photoBack, String photoID) {
        this.username = username;
        this.photoFront = photoFront;
        this.photoBack = photoBack;
        this.photoIDCard = photoID;
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

    public String getPhotoIDCard() {
        return photoIDCard;
    }
}
