package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.Account;
import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.model.DTO.DriverDTO;
import com.example.vehiclesharingsystemserver.model.Driver;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DTOConverter {

    private final PasswordEncoder passwordEncoder;

    public DTOConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    public Driver fromDTOtoDriver(DriverDTO driverDTO){
        return new Driver(driverDTO.getFirstName(), driverDTO.getLastName(),fromDTOtoAccount(driverDTO.getAccount()));
    }
    public Account fromDTOtoAccount(AccountDTO accountDTO){
        return new Account(accountDTO.getUsername(), passwordEncoder.encode(accountDTO.getPassword()), accountDTO.getPhoneNumber(), accountDTO.getEmailAddress(), accountDTO.getAccountType());
    }
}

