package com.example.vehiclesharingsystemserver.controller;

import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.model.DTO.UserDTO;
import com.example.vehiclesharingsystemserver.service.DriverOperationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
public class DriverController {

    private final DriverOperationsService driverOperationsService;

    public DriverController(DriverOperationsService driverOperationsService) {
        this.driverOperationsService = driverOperationsService;
    }

    @PostMapping("/driver/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(driverOperationsService.register(userDTO));
    }
    @PostMapping("/driver/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AccountDTO accountDTO){
        try{
            String result = driverOperationsService.authenticate(accountDTO);
            return ResponseEntity.ok(result);
        }
        catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong credentials");
        }

    }
    @PostMapping("/driver/checkUsername")
    public ResponseEntity<String> checkUsername(@RequestBody String username){
        return ResponseEntity.ok(driverOperationsService.checkIfUsernameExists(username));
    }
    @GetMapping("/driver/securedEndpoint")
    public ResponseEntity<String> se(){
        return ResponseEntity.ok("Ciao secured.");
    }
}
