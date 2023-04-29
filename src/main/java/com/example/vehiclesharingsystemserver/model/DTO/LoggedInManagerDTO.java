package com.example.vehiclesharingsystemserver.model.DTO;

public class LoggedInManagerDTO {
    private final String token;
    private final String companyName;

    public String getToken() {
        return token;
    }

    public String getCompanyName() {
        return companyName;
    }

    public LoggedInManagerDTO(String token, String companyName) {
        this.token = token;
        this.companyName = companyName;
    }
}
