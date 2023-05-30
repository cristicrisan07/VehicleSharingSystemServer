package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.*;
import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.repository.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdministratorOperationsService {
    private final AccountRepository accountRepository;
    private final AdministratorRepository administratorRepository;
    private final RentalCompanyManagerRepository rentalCompanyManagerRepository;
    private final CompanyRepository companyRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final IdentityValidationDocumentRepository identityValidationDocumentRepository;
    private final DTOConverter dtoConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AdministratorOperationsService(AccountRepository accountRepository, AdministratorRepository administratorRepository, RentalCompanyManagerRepository rentalCompanyManagerRepository, CompanyRepository companyRepository, SubscriptionRepository subscriptionRepository, IdentityValidationDocumentRepository identityValidationDocumentRepository, DTOConverter dtoConverter, AuthenticationManager authenticationManager, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.administratorRepository = administratorRepository;
        this.rentalCompanyManagerRepository = rentalCompanyManagerRepository;
        this.companyRepository = companyRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.identityValidationDocumentRepository = identityValidationDocumentRepository;
        this.dtoConverter = dtoConverter;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(UserDTO userDTO){

        String status = checkForExistentUser(userDTO);
        if(!Objects.equals(status, "DOESN'T EXIST")){
            return status;
        }
        var administrator = dtoConverter.fromUserDTOtoAdministrator(userDTO);
        accountRepository.save(administrator.getAccount());
        administratorRepository.save(administrator);
        return jwtService.generateToken(new HashMap<>(), administrator.getAccount());



    }

    public String checkIfUsernameExists(String username) {
        if (accountRepository.findByUsername(username).isPresent()) {
            return "ERROR: USERNAME_TAKEN";
        } else {
            return "DOESN'T EXIST";
        }
    }
    public String checkForExistentUser(UserDTO userDTO){
        String status = checkIfUsernameExists(userDTO.getAccount().getUsername());
        if(Objects.equals(status, "DOESN'T EXIST")) {
            if (accountRepository.findByEmailAddress(userDTO.getAccount().getEmailAddress()).isPresent()) {
                return "ERROR: EMAIL_TAKEN";
            }
            else{
                if(accountRepository.findAccountByPhoneNumber(userDTO.getAccount().getPhoneNumber()).isPresent()){
                    return "ERROR: PHONE_NUMBER_TAKEN";
                }
            }
        }
            return status;
    }

    public String authenticate(AccountDTO accountDTO) throws NoSuchElementException{
        var account = accountRepository.findByUsername(accountDTO.getUsername()).orElseThrow();
        if(Objects.equals(account.getAccountType(),"administrator")){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountDTO.getUsername(),accountDTO.getPassword()));
            return jwtService.generateToken(new HashMap<>(),account);
        }
        else{
            throw new NoSuchElementException();
        }

    }

    public String addCompany(CompanyDTO companyDTO){
        if(companyRepository.findCompaniesByName(companyDTO.getName()).isPresent()){
            return "ERROR: NAME_TAKEN";
        }
        else{
            if(companyRepository.findCompaniesByEmailAddress(companyDTO.getEmailAddress()).isPresent()){
                return "ERROR: EMAIL_ADDRESS_TAKEN";
            }
            else {
                if(companyRepository.findCompaniesByPhoneNumber(companyDTO.getPhoneNumber()).isPresent()){
                    return "ERROR: PHONE_NUMBER_TAKEN";
                }
                else {
                    companyRepository.save(dtoConverter.fromDTOtoCompany(companyDTO));
                    return "SUCCESS";
                }
            }
        }
    }
    public String addManager(RentalCompanyManagerDTO rentalCompanyManagerDTO){

        String status = checkForExistentUser(rentalCompanyManagerDTO.getUserDTO());
        if(!Objects.equals(status, "DOESN'T EXIST")){
            return status;
        }
        var rentalCompanyManager = dtoConverter.fromDTOtoRentalCompanyManager(rentalCompanyManagerDTO);
        accountRepository.save(rentalCompanyManager.getAccount());
        rentalCompanyManagerRepository.save(rentalCompanyManager);
        return "SUCCESS";

    }

    public String addSubscription(SubscriptionDTO subscriptionDTO){
        boolean status = subscriptionRepository.findSubscriptionByName(subscriptionDTO.getName()).isPresent();
        if(status){
            return "ERROR: NAME_TAKEN";
        }
        else{
            subscriptionRepository.save(dtoConverter.fromDTOtoSubscription(subscriptionDTO));
            return subscriptionRepository.findSubscriptionByName(subscriptionDTO.getName()).get().getId().toString();
        }
    }
    public List<CompanyDTO> getCompanies(){
        Iterable<Company> iterableCompanies = companyRepository.findAll();
        ArrayList<Company> companies = new ArrayList<>();
        iterableCompanies.forEach(companies::add);
        return companies.stream().map(dtoConverter::fromCompanyToDTO).toList();
    }

    public List<RentalCompanyManagerDTO> getManagers(){
        Iterable<RentalCompanyManager> iterableManagers = rentalCompanyManagerRepository.findAll();
        ArrayList<RentalCompanyManager> managers = new ArrayList<>();
        iterableManagers.forEach(managers::add);
        return managers.stream().map(dtoConverter::fromRentalCompanyManagerToDTO).toList();
    }

    public List<SubscriptionDTO> getAvailableSubscriptions(){
        Iterable<Subscription> iterableSubscriptions = subscriptionRepository.findAll();
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        iterableSubscriptions.forEach(subscriptions::add);
        return subscriptions.stream().map(dtoConverter::fromSubscriptionToDTO).toList();
    }

    public String updateCompany(CompanyDTO companyDTO){
        Optional<Company> databaseCompany = companyRepository.findCompaniesByName(companyDTO.getName());
        if(databaseCompany.isEmpty()){
            return "ERROR: COULD_NOT_FIND_COMPANY";
        }
        else{
            if(!Objects.equals(companyDTO.getEmailAddress(), databaseCompany.get().getEmailAddress()) && companyRepository.findCompaniesByEmailAddress(companyDTO.getEmailAddress()).isPresent()){
                return "ERROR: EMAIL_ADDRESS_TAKEN";
            }
            else {
                if(!Objects.equals(companyDTO.getPhoneNumber(), databaseCompany.get().getPhoneNumber()) && companyRepository.findCompaniesByPhoneNumber(companyDTO.getPhoneNumber()).isPresent()){
                    return "ERROR: PHONE_NUMBER_TAKEN";
                }
                else {
                    databaseCompany.get().setEmailAddress(companyDTO.getEmailAddress());
                    databaseCompany.get().setPhoneNumber(companyDTO.getPhoneNumber());
                    companyRepository.save(databaseCompany.get());
                    return "SUCCESS";
                }
            }
        }
    }
    public String updateManager(RentalCompanyManagerDTO rentalCompanyManagerDTO) {
        Optional<Account> account = accountRepository.findByUsername(rentalCompanyManagerDTO.getUserDTO().getAccount().getUsername());
        if (account.isEmpty()) {
            return "ERROR: COULD_NOT_FIND_ACCOUNT";
        }
        else {
            if (!Objects.equals(rentalCompanyManagerDTO.getUserDTO().getAccount().getEmailAddress(), account.get().getEmailAddress()) &&
                    accountRepository.findByEmailAddress(rentalCompanyManagerDTO.getUserDTO().getAccount().getEmailAddress()).isPresent()){
            return "ERROR: EMAIL_TAKEN";
            }
            else{
            if (!Objects.equals(rentalCompanyManagerDTO.getUserDTO().getAccount().getPhoneNumber(), account.get().getPhoneNumber()) &&
                    accountRepository.findAccountByPhoneNumber(rentalCompanyManagerDTO.getUserDTO().getAccount().getPhoneNumber()).isPresent()) {
                return "ERROR: PHONE_NUMBER_TAKEN";
            }
            }

            Optional<RentalCompanyManager> databaseRentalCompanyManager = rentalCompanyManagerRepository.findRentalCompanyManagerByAccount(account.get());
            if(databaseRentalCompanyManager.isPresent()) {
                if(!Objects.equals(rentalCompanyManagerDTO.getUserDTO().getAccount().getPassword(),"")){
                    account.get().setPassword(passwordEncoder.encode(rentalCompanyManagerDTO.getUserDTO().getAccount().getPassword()));
                }
                account.get().setEmailAddress(rentalCompanyManagerDTO.getUserDTO().getAccount().getEmailAddress());
                account.get().setPhoneNumber(rentalCompanyManagerDTO.getUserDTO().getAccount().getPhoneNumber());
                accountRepository.save(account.get());
                databaseRentalCompanyManager.get().setFirstName(rentalCompanyManagerDTO.getUserDTO().getFirstName());
                databaseRentalCompanyManager.get().setLastName(rentalCompanyManagerDTO.getUserDTO().getLastName());
                rentalCompanyManagerRepository.save(databaseRentalCompanyManager.get());
                return "SUCCESS";
            }else{
                return "ERROR: COULD_NOT_FIND_MANAGER";
            }

        }
    }

    public String updateSubscription(SubscriptionDTO subscriptionDTO) {

        Optional<Subscription> subscription = subscriptionRepository.findById(UUID.fromString(subscriptionDTO.getId()));
        if (subscription.isEmpty()) {
            return "ERROR: COULD_NOT_FIND_SUBSCRIPTION_WITH_THIS_ID";
        } else {
            if (!Objects.equals(subscriptionDTO.getName(), subscription.get().getName()) &&
                    subscriptionRepository.findSubscriptionByName(subscriptionDTO.getName()).isPresent()) {
                return "ERROR: NAME_TAKEN";
            }
        }

        subscription.get().setName(subscriptionDTO.getName());
        subscription.get().setRentalPrice(dtoConverter.fromDTOtoRentalPrice(subscriptionDTO.getRentalPriceDTO()));
        subscription.get().setKilometersLimit(subscriptionDTO.getKilometersLimit());
        subscriptionRepository.save(subscription.get());
        return "SUCCESS";
    }

    public String deleteSubscription(String id){
        Optional<Subscription> subscription = subscriptionRepository.findById(UUID.fromString(id));
        if(subscription.isEmpty()){
            return "ERROR: COULD_NOT_FIND_SUBSCRIPTION_WITH_THIS_ID";
        }
        else{
            subscriptionRepository.delete(subscription.get());
            return "SUCCESS";
        }
    }
    public String deleteManager(String username){
        Optional<Account> databaseAccount = accountRepository.findByUsername(username);
        if(databaseAccount.isPresent()) {
            Optional<RentalCompanyManager> databaseManager = rentalCompanyManagerRepository.findRentalCompanyManagerByAccount(databaseAccount.get());
            if(databaseManager.isPresent()){
                rentalCompanyManagerRepository.delete(databaseManager.get());
                accountRepository.delete(databaseAccount.get());
                return "SUCCESS";
            }else{
                return "ERROR: COULD_NOT_FIND_MANAGER";
            }
        }
        else{
            return "ERROR: USERNAME_DOESN'T_EXIST";
        }
    }
    public String deleteCompany(String name){
        Optional<Company> databaseCompany = companyRepository.findCompaniesByName(name);
        if(databaseCompany.isEmpty()){
            return "ERROR: COULD_NOT_FIND_COMPANY";
        }else{
            for(RentalCompanyManagerDTO rentalCompanyManagerDTO:getManagers()){
                if(Objects.equals(rentalCompanyManagerDTO.getCompanyName(),name)){
                    return "ERROR: MANAGER_FOUND. PLEASE DELETE ALL MANAGERS OF THIS COMPANY BEFORE DELETING IT.";
                }
            }
            companyRepository.delete(databaseCompany.get());
            return "SUCCESS";
        }
    }

    public List<IdentityValidationDocumentDTO> getPendingValidations(){
        var iterableIdentities = identityValidationDocumentRepository.findAllByStatus("PENDING_VALIDATION");
        ArrayList<IdentityValidationDocument> identities = new ArrayList<>();
        iterableIdentities.forEach(identities::add);
        return identities.stream().map(dtoConverter::fromIdentityValidationDocumentToDTO).toList();

    }

}
