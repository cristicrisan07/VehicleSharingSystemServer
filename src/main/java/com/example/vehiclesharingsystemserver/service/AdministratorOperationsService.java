package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.Company;
import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.model.DTO.CompanyDTO;
import com.example.vehiclesharingsystemserver.model.DTO.RentalCompanyManagerDTO;
import com.example.vehiclesharingsystemserver.model.DTO.UserDTO;
import com.example.vehiclesharingsystemserver.model.RentalCompanyManager;
import com.example.vehiclesharingsystemserver.repository.AccountRepository;
import com.example.vehiclesharingsystemserver.repository.AdministratorRepository;
import com.example.vehiclesharingsystemserver.repository.CompanyRepository;
import com.example.vehiclesharingsystemserver.repository.RentalCompanyManagerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdministratorOperationsService {
    private final AccountRepository accountRepository;
    private final AdministratorRepository administratorRepository;
    private final RentalCompanyManagerRepository rentalCompanyManagerRepository;
    private final CompanyRepository companyRepository;
    private final DTOConverter dtoConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AdministratorOperationsService(AccountRepository accountRepository, AdministratorRepository administratorRepository, RentalCompanyManagerRepository rentalCompanyManagerRepository, CompanyRepository companyRepository, DTOConverter dtoConverter, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.administratorRepository = administratorRepository;
        this.rentalCompanyManagerRepository = rentalCompanyManagerRepository;
        this.companyRepository = companyRepository;
        this.dtoConverter = dtoConverter;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
            return "USERNAME_TAKEN";
        } else {
            return "DOESN'T EXIST";
        }
    }
    public String checkForExistentUser(UserDTO userDTO){
        String status = checkIfUsernameExists(userDTO.getAccount().getUsername());
        if(Objects.equals(status, "DOESN'T EXIST")) {
            if (accountRepository.findByEmailAddress(userDTO.getAccount().getEmailAddress()).isPresent()) {
                return "EMAIL_TAKEN";
            }
        }
            return status;
    }

    public String authenticate(AccountDTO accountDTO){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountDTO.getUsername(),accountDTO.getPassword()));
        var account = accountRepository.findByUsername(accountDTO.getUsername()).orElseThrow();
        return jwtService.generateToken(new HashMap<>(),account);
    }

    public String addCompany(CompanyDTO companyDTO){
        if(companyRepository.findCompaniesByName(companyDTO.getName()).isPresent()){
            return "ERROR: NAME_TAKEN";
        }
        else{
            companyRepository.save(dtoConverter.fromDTOtoCompany(companyDTO));
            return "SUCCESS";
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
        return jwtService.generateToken(new HashMap<>(), rentalCompanyManager.getAccount());

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

}
