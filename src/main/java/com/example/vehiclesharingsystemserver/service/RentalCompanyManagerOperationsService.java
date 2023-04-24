package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.repository.AccountRepository;
import com.example.vehiclesharingsystemserver.repository.RentalCompanyManagerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class RentalCompanyManagerOperationsService {
    private final RentalCompanyManagerRepository rentalCompanyManagerRepository;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public RentalCompanyManagerOperationsService(RentalCompanyManagerRepository rentalCompanyManagerRepository, AccountRepository accountRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.rentalCompanyManagerRepository = rentalCompanyManagerRepository;
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String authenticate(AccountDTO accountDTO) throws NoSuchElementException {
        var account = accountRepository.findByUsername(accountDTO.getUsername()).orElseThrow();
        if(Objects.equals(account.getAccountType(),"manager")){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountDTO.getUsername(),accountDTO.getPassword()));
            return jwtService.generateToken(new HashMap<>(),account);
        }
        else{
            throw new NoSuchElementException();
        }

    }
}
