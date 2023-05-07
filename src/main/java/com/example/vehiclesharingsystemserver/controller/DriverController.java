package com.example.vehiclesharingsystemserver.controller;

import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.model.DTO.SubscriptionContractDTO;
import com.example.vehiclesharingsystemserver.model.DTO.UserDTO;
import com.example.vehiclesharingsystemserver.model.DTO.VehicleDTO;
import com.example.vehiclesharingsystemserver.service.DriverOperationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @GetMapping("/driver/getAllVehicles")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(){
        return ResponseEntity.ok(driverOperationsService.getAllVehicles());
    }
    @PostMapping("/driver/addSubscriptionToDriver")
    public ResponseEntity<String> addSubscriptionToDriver(@RequestBody SubscriptionContractDTO subscriptionContractDTO){
        try{
            String result = driverOperationsService.addSubscriptionToDriver(subscriptionContractDTO);
            return ResponseEntity.ok(result);
        }catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("DRIVER_NOT_FOUND");
        }

    }




}
