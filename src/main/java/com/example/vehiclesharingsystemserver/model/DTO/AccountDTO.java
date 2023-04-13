package com.example.vehiclesharingsystemserver.model.DTO;

public class AccountDTO {

    private final String username;
    private final String password;
    private final String phoneNumber;
    private final String emailAddress;
    private final String accountType;

    public AccountDTO(String username, String password, String phoneNumber, String emailAddress, String accountType) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.accountType = accountType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getAccountType() {
        return accountType;
    }
}
