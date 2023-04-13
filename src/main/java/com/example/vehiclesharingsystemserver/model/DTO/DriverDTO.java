package com.example.vehiclesharingsystemserver.model.DTO;

public class DriverDTO {
    private final String firstName;
    private final String lastName;
    private final AccountDTO account;


    public DriverDTO(String firstName, String lastName, AccountDTO account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.account = account;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public AccountDTO getAccount() {
        return account;
    }
}
