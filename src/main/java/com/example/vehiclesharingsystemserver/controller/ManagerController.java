package com.example.vehiclesharingsystemserver.controller;

import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.service.RentalCompanyManagerOperationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class ManagerController {
    private final RentalCompanyManagerOperationsService rentalCompanyManagerOperationsService;

    public ManagerController(RentalCompanyManagerOperationsService rentalCompanyManagerOperationsService) {
        this.rentalCompanyManagerOperationsService = rentalCompanyManagerOperationsService;
    }

    @PostMapping("/manager/authenticate")
    public ResponseEntity<LoggedInManagerDTO> authenticate(@RequestBody AccountDTO accountDTO){
        try {
            LoggedInManagerDTO result = rentalCompanyManagerOperationsService.authenticate(accountDTO);
            return ResponseEntity.ok(result);
        }
        catch (NoSuchElementException e)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new LoggedInManagerDTO("",""));
        }
    }
    @PostMapping("/manager/addVehicle")
    public ResponseEntity<String> addVehicle(@RequestBody VehicleDTO vehicleDTO){
        return ResponseEntity.ok(rentalCompanyManagerOperationsService.addVehicle(vehicleDTO));
    }

    @GetMapping("/manager/getVehiclesOfCompany/{companyName}")
    public ResponseEntity<List<VehicleDTO>> getVehiclesOfCompany(@PathVariable String companyName){
        List<VehicleDTO> vehicles = rentalCompanyManagerOperationsService.getVehiclesOfCompany(companyName);
        return ResponseEntity.status(HttpStatus.OK).body(vehicles);
    }
    @PostMapping("/manager/updateVehicle")
    public ResponseEntity<String> updateVehicle(@RequestBody VehicleDTO vehicleDTO){
        return ResponseEntity.ok(rentalCompanyManagerOperationsService.updateVehicle(vehicleDTO));
    }
    @PostMapping("/manager/performEmergencyAction")
    public ResponseEntity<String> performEmergencyAction(@RequestBody EmergencyActionDTOForWeb emergencyActionDTOForWeb){
        try{
           String status = rentalCompanyManagerOperationsService.createEmergencyIntervention(emergencyActionDTOForWeb);
           return ResponseEntity.ok(status);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @GetMapping("/manager/getEmergencyInterventionStatus/{vin}")
    public ResponseEntity<String> getEmergencyInterventionStatus(@PathVariable String vin){
        try{
            String status = rentalCompanyManagerOperationsService.getEmergencyInterventionStatus(vin);
            return ResponseEntity.ok(status);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @DeleteMapping("/manager/deleteVehicle/{vin}")
    public ResponseEntity<String> deleteVehicle(@PathVariable String vin){
        return ResponseEntity.ok(rentalCompanyManagerOperationsService.deleteVehicle(vin));
    }
    @GetMapping("/manager/getVehicleLocation/{vin}")
    public ResponseEntity<String> getVehicleLocation(@PathVariable String vin){
        return ResponseEntity.ok(rentalCompanyManagerOperationsService.getVehicleLocation(vin));
    }
}
