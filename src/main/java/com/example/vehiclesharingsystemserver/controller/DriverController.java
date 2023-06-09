package com.example.vehiclesharingsystemserver.controller;

import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.service.DriverOperationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Blob;
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
        String result = driverOperationsService.register(userDTO);
        return result.contains("ERROR") ?
                ResponseEntity.ok(result)
                :
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
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
    @GetMapping("/driver/getAllAvailableVehicles")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles(){
        return ResponseEntity.ok(driverOperationsService.getAllAvailableVehicles());
    }
    @PostMapping("/driver/addSubscriptionToDriver")
    public ResponseEntity<ActiveSubscriptionDTO> addSubscriptionToDriver(@RequestBody SubscriptionContractDTO subscriptionContractDTO){
        try{
            ActiveSubscriptionDTO result = driverOperationsService.addSubscriptionToDriver(subscriptionContractDTO);
            return ResponseEntity.ok(result);
        }catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new ActiveSubscriptionDTO(
                            e.getMessage(),
                            new SubscriptionDTO(
                                    "",
                                    "",
                                    0,
                                    new RentalPriceDTO("","","")),
                            "",
                            "")
                    );
        }
    }

    @GetMapping("/driver/getDriverSubscription/{username}")
    public ResponseEntity<ActiveSubscriptionDTO> getDriverSubscription(@PathVariable String username){
        try{
            ActiveSubscriptionDTO activeSubscriptionDTO = driverOperationsService.getDriverSubscription(username);
            return ResponseEntity.ok(activeSubscriptionDTO);
        }catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        }
    }

    @PostMapping("/driver/startRentalSession")
    public ResponseEntity<String> startRentalSession(@RequestBody RentalSessionDTO rentalSessionDTO) {
        try{
            String status = driverOperationsService.startRentalSession(rentalSessionDTO);
            return ResponseEntity.ok(status);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PutMapping("/driver/endRentalSession")
    public ResponseEntity<String> endRentalSession(@RequestBody RentalSessionDTO rentalSessionDTO) {
        try{
            String status = driverOperationsService.endRentalSession(rentalSessionDTO);
            return ResponseEntity.ok(status);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PutMapping("/driver/payRentalSession")
    public ResponseEntity<String> payRentalSession(@RequestBody PaymentDTO paymentDTO) {
        try{
            String status = driverOperationsService.payForRentalSession(paymentDTO);
            return ResponseEntity.ok(status);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PutMapping("/driver/updateRentalSession")
    public ResponseEntity<String> updateRentalSession(@RequestBody RentalSessionDTO rentalSessionDTO){
        try{
            String status = driverOperationsService.updateRentalSession(rentalSessionDTO);
            return ResponseEntity.ok(status);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @GetMapping("/driver/getCurrentRentalSession/{username}")
    public ResponseEntity<CurrentRentalSessionDTO> getCurrentRentalSession(@PathVariable String username){
        try{
            CurrentRentalSessionDTO currentRentalSessionDTO = driverOperationsService.getCurrentRentalSession(username);
            return ResponseEntity.ok(currentRentalSessionDTO);
        }catch(NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(null);
        }
    }

    @PostMapping("/driver/setDriverLicense")
    public ResponseEntity<String> setDriverLicense(@RequestBody IdentityValidationDocumentDTO identityValidationDocumentDTO) {
        try{
            String status = driverOperationsService.setDrivingLicensePhoto(identityValidationDocumentDTO);
            return ResponseEntity.ok(status);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }

    @PostMapping("/driver/hasSubmittedDocuments")
    public ResponseEntity<String> hasSubmittedDocuments(@RequestBody String username){
        return ResponseEntity.ok(driverOperationsService.hasSubmittedDocuments(username));
    }

    @PostMapping("/driver/isValidatedAsLegalDriver")
    public ResponseEntity<String> isValidated(@RequestBody String username){
        return ResponseEntity.ok(driverOperationsService.isValidatedAsLegalDriver(username));
    }





}
