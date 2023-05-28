package com.example.vehiclesharingsystemserver.model.DTO;

public class DocumentStatusDTO {
    private final String username;
    private final String status;
    public DocumentStatusDTO(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }
}
