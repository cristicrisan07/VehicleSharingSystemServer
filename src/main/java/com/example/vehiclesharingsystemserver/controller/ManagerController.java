package com.example.vehiclesharingsystemserver.controller;

import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.service.RentalCompanyManagerOperationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
public class ManagerController {
    private final RentalCompanyManagerOperationsService rentalCompanyManagerOperationsService;

    public ManagerController(RentalCompanyManagerOperationsService rentalCompanyManagerOperationsService) {
        this.rentalCompanyManagerOperationsService = rentalCompanyManagerOperationsService;
    }

    @PostMapping("/manager/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AccountDTO accountDTO){
        try {
            String result = rentalCompanyManagerOperationsService.authenticate(accountDTO);
            return ResponseEntity.ok(result);
        }
        catch (NoSuchElementException e)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong credentials");
        }
    }
}
