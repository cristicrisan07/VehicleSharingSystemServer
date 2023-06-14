package com.example.vehiclesharingsystemserver.controller;

import com.example.vehiclesharingsystemserver.model.DTO.VehicleControllerLocationDTO;
import com.example.vehiclesharingsystemserver.model.DTO.VehicleControllerStateDTO;
import com.example.vehiclesharingsystemserver.service.VehicleOperationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleController {
    private final VehicleOperationsService vehicleOperationsService;

    public VehicleController(VehicleOperationsService vehicleOperationsService) {
        this.vehicleOperationsService = vehicleOperationsService;
    }

    @GetMapping("/vehicle/getCurrentRentalSession/{vin}")
    public ResponseEntity<VehicleControllerStateDTO> getCurrentRentalSession(@PathVariable String vin) {
        try {
            var result = vehicleOperationsService.getCurrentRentalSession(vin);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
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
