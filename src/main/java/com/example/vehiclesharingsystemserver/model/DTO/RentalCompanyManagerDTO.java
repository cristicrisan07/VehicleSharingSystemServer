package com.example.vehiclesharingsystemserver.model.DTO;

public class RentalCompanyManagerDTO {

    private final UserDTO userDTO;
    private final String companyName;

    public RentalCompanyManagerDTO(UserDTO userDTO, String companyName) {
        this.userDTO = userDTO;
        this.companyName = companyName;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public String getCompanyName() {
        return companyName;
    }
}
