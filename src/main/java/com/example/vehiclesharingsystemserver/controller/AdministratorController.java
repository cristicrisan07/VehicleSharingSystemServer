package com.example.vehiclesharingsystemserver.controller;

import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.service.AdministratorOperationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class AdministratorController {

    private final AdministratorOperationsService administratorOperationsService;

    public AdministratorController(AdministratorOperationsService administratorOperationsService) {
        this.administratorOperationsService = administratorOperationsService;
    }
    @PostMapping("/administrator/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(administratorOperationsService.register(userDTO));
    }
    @PostMapping("/administrator/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AccountDTO accountDTO){
        try {
            String result = administratorOperationsService.authenticate(accountDTO);
            return ResponseEntity.ok(result);
        }
        catch (NoSuchElementException e)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong credentials");
        }
    }
    @PostMapping("/administrator/addCompany")
    public ResponseEntity<String> addCompany(@RequestBody CompanyDTO companyDTO){
        return ResponseEntity.ok(administratorOperationsService.addCompany(companyDTO));
    }
    @PostMapping("/administrator/addManager")
    public ResponseEntity<String> addManager(@RequestBody RentalCompanyManagerDTO rentalCompanyManagerDTO){
        return ResponseEntity.ok(administratorOperationsService.addManager(rentalCompanyManagerDTO));
    }
    @PostMapping("/administrator/addSubscription")
    public ResponseEntity<String> addSubscription(@RequestBody SubscriptionDTO subscriptionDTO){
        return ResponseEntity.ok(administratorOperationsService.addSubscription(subscriptionDTO));
    }
    @GetMapping( "/administrator/getCompanies")
    public ResponseEntity<List<CompanyDTO>> getCompanies(){
        List<CompanyDTO> companies = administratorOperationsService.getCompanies();
        return ResponseEntity.status(companies.isEmpty() ? HttpStatus.NOT_ACCEPTABLE : HttpStatus.OK).body(companies);
    }
    @GetMapping("/administrator/getManagers")
    public ResponseEntity<List<RentalCompanyManagerDTO>> getManagers(){
        List<RentalCompanyManagerDTO> managers = administratorOperationsService.getManagers();
        return ResponseEntity.status(HttpStatus.OK).body(managers);
    }
    @GetMapping("/administrator/getAvailableSubscriptions")
    public ResponseEntity<List<SubscriptionDTO>> getSubscription(){
        List<SubscriptionDTO> subscriptions = administratorOperationsService.getAvailableSubscriptions();
        return ResponseEntity.status(HttpStatus.OK).body(subscriptions);
    }

    @GetMapping("/administrator/getPendingValidations")
    public ResponseEntity<List<IdentityValidationDocumentDTO>> getPendingValidation(){
        List<IdentityValidationDocumentDTO> pendingValidations = administratorOperationsService.getPendingValidations();
        return ResponseEntity.status(HttpStatus.OK).body(pendingValidations);
    }
    @PostMapping("/administrator/updateSubscription")
    public ResponseEntity<String> updateSubscription(@RequestBody SubscriptionDTO subscriptionDTO){
        return ResponseEntity.ok(administratorOperationsService.updateSubscription(subscriptionDTO));
    }
    @PostMapping("/administrator/updateCompany")
    public ResponseEntity<String> updateCompany(@RequestBody CompanyDTO companyDTO){
        return ResponseEntity.ok(administratorOperationsService.updateCompany(companyDTO));
    }
    @PostMapping("/administrator/updateManager")
    public ResponseEntity<String> updateManager(@RequestBody RentalCompanyManagerDTO rentalCompanyManagerDTO){
        return ResponseEntity.ok(administratorOperationsService.updateManager(rentalCompanyManagerDTO));
    }
    @DeleteMapping("/administrator/deleteManager/{username}")
    public ResponseEntity<String> deleteManager(@PathVariable String username){
        return ResponseEntity.ok(administratorOperationsService.deleteManager(username));
    }
    @DeleteMapping("/administrator/deleteCompany/{name}")
    public ResponseEntity<String> deleteCompany(@PathVariable String name){
        return ResponseEntity.ok(administratorOperationsService.deleteCompany(name));
    }
    @DeleteMapping("/administrator/deleteSubscription/{id}")
    public ResponseEntity<String> deleteSubscription(@PathVariable String id){
        return ResponseEntity.ok(administratorOperationsService.deleteSubscription(id));
    }
    @PostMapping("/administrator/setStatusOfDriverDocument")
    public ResponseEntity<String> setStatusOfDriverDocument(@RequestBody DocumentStatusDTO documentStatusDTO){
        try{
            String status = administratorOperationsService.setStatusOfDriverDocument(documentStatusDTO);
            return ResponseEntity.ok(status);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
        }
    }







}
