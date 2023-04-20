package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.*;
import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.model.DTO.CompanyDTO;
import com.example.vehiclesharingsystemserver.model.DTO.RentalCompanyManagerDTO;
import com.example.vehiclesharingsystemserver.model.DTO.UserDTO;
import com.example.vehiclesharingsystemserver.repository.CompanyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DTOConverter {

    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    public DTOConverter(PasswordEncoder passwordEncoder, CompanyRepository companyRepository) {
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
    }


    public Driver fromUserDTOtoDriver(UserDTO userDTO) {
        return new Driver(userDTO.getFirstName(), userDTO.getLastName(), fromDTOtoAccount(userDTO.getAccount()));
    }

    public Administrator fromUserDTOtoAdministrator(UserDTO userDTO) {
        return new Administrator(userDTO.getFirstName(), userDTO.getLastName(), fromDTOtoAccount(userDTO.getAccount()));
    }

    public Account fromDTOtoAccount(AccountDTO accountDTO) {
        return new Account(accountDTO.getUsername(), passwordEncoder.encode(accountDTO.getPassword()),
                accountDTO.getPhoneNumber(), accountDTO.getEmailAddress(), accountDTO.getAccountType());
    }
    public AccountDTO fromAccountToDTO(Account account){
        return new AccountDTO(account.getUsername(),"",account.getPhoneNumber(), account.getEmailAddress(),
                account.getAccountType());

    }

    public Company fromDTOtoCompany(CompanyDTO companyDTO) {
        return new Company(companyDTO.getName(), companyDTO.getEmailAddress(), companyDTO.getPhoneNumber());
    }

    public CompanyDTO fromCompanyToDTO(Company company){
        return new CompanyDTO(company.getName(), company.getEmailAddress(), company.getPhoneNumber());
    }

    public RentalCompanyManager fromDTOtoRentalCompanyManager(RentalCompanyManagerDTO rentalCompanyManagerDTO) {
        Optional<Company> company = companyRepository.findCompaniesByName(rentalCompanyManagerDTO.getCompanyName());
        return company.map(value -> new RentalCompanyManager(rentalCompanyManagerDTO.getUserDTO().getFirstName(),
                rentalCompanyManagerDTO.getUserDTO().getLastName(),
                fromDTOtoAccount(rentalCompanyManagerDTO.getUserDTO().getAccount()), value)).orElse(null);
    }

    public RentalCompanyManagerDTO fromRentalCompanyManagerToDTO(RentalCompanyManager rentalCompanyManager){
        return new RentalCompanyManagerDTO(new UserDTO(rentalCompanyManager.getFirstName(),
                rentalCompanyManager.getLastName(),
                fromAccountToDTO(rentalCompanyManager.getAccount())),
                rentalCompanyManager.getCompany().getName());
    }
}

