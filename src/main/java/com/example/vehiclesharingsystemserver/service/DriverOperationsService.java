package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.model.DTO.UserDTO;
import com.example.vehiclesharingsystemserver.repository.AccountRepository;
import com.example.vehiclesharingsystemserver.repository.DriverRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class DriverOperationsService {

    private final AccountRepository accountRepository;
    private final DriverRepository driverRepository;
    private final DTOConverter dtoConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public DriverOperationsService(AccountRepository accountRepository, DriverRepository driverRepository, DTOConverter dtoConverter, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.dtoConverter = dtoConverter;
        this.driverRepository = driverRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String register(UserDTO userDTO){

         String status = checkIfUsernameExists(userDTO.getAccount().getUsername());
         if(Objects.equals(status, "DOESN'T EXIST")) {
             if (accountRepository.findByEmailAddress(userDTO.getAccount().getEmailAddress()).isPresent()) {
                 return "EMAIL_TAKEN";
             }
             var driver = dtoConverter.fromUserDTOtoDriver(userDTO);
             accountRepository.save(driver.getAccount());
             driverRepository.save(driver);
             return jwtService.generateToken(new HashMap<>(), driver.getAccount());
         }
         else {
             return "ERROR: " + status;
         }

    }

    public String checkIfUsernameExists(String username) {
        if (accountRepository.findByUsername(username).isPresent()) {
            return "USERNAME_TAKEN";
        } else {
            return "DOESN'T EXIST";
        }
    }

    public String authenticate(AccountDTO accountDTO){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountDTO.getUsername(),accountDTO.getPassword()));
        var account = accountRepository.findByUsername(accountDTO.getUsername()).orElseThrow();
        return jwtService.generateToken(new HashMap<>(),account);
    }

}
