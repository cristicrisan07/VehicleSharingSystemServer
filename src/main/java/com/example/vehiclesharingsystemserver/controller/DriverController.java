package com.example.vehiclesharingsystemserver.controller;

import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.model.DTO.DriverDTO;
import com.example.vehiclesharingsystemserver.service.DriverOperationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DriverController {

    private final DriverOperationsService driverOperationsService;

    public DriverController(DriverOperationsService driverOperationsService) {
        this.driverOperationsService = driverOperationsService;
    }

    @PostMapping("/driver/register")
    public ResponseEntity<String> register(@RequestBody DriverDTO driverDTO){
        return ResponseEntity.ok(driverOperationsService.register(driverDTO));
    }
    @PostMapping("/driver/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AccountDTO accountDTO){
        return ResponseEntity.ok(driverOperationsService.authenticate(accountDTO));
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
