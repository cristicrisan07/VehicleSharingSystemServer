package com.example.vehiclesharingsystemserver.controller;

import com.example.vehiclesharingsystemserver.model.DTO.CurrentRentalSessionDTO;
import com.example.vehiclesharingsystemserver.model.DTO.VehicleControllerLocationDTO;
import com.example.vehiclesharingsystemserver.service.VehicleOperationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.NoSuchElementException;

@RestController
public class VehicleController {
    private final VehicleOperationsService vehicleOperationsService;

    public VehicleController(VehicleOperationsService vehicleOperationsService) {
        this.vehicleOperationsService = vehicleOperationsService;
    }

    @PostMapping("/vehicle/getCurrentRentalSession")
    public ResponseEntity<String> getCurrentRentalSession(@RequestBody String vin) {
        try {
            String result = vehicleOperationsService.getCurrentRentalSession(vin);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/vehicle/setCurrentLocation")
    public ResponseEntity<String> setCurrentLocation(@RequestBody VehicleControllerLocationDTO vehicleControllerLocationDTO){
        try {
            String result = vehicleOperationsService.setCurrentLocation(vehicleControllerLocationDTO);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/vehicle/getCurrentLocation")
    public ResponseEntity<String> getCurrentLocation(@RequestBody String vin){
        try {
            String result = vehicleOperationsService.getCurrentLocation(vin);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(e.getMessage());
        }
    }
}
