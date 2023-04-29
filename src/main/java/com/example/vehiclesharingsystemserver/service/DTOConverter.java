package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.*;
import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.repository.CompanyRepository;
import com.example.vehiclesharingsystemserver.repository.RentalPriceRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class DTOConverter {

    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final RentalPriceRepository rentalPriceRepository;

    public DTOConverter(PasswordEncoder passwordEncoder, CompanyRepository companyRepository, RentalPriceRepository rentalPriceRepository) {
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.rentalPriceRepository = rentalPriceRepository;
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

    public RentalPrice fromDTOtoRentalPrice(RentalPriceDTO rentalPriceDTO) throws NumberFormatException{
        var timeUnit = TimeUnit.HOURS;
        if( Objects.equals(rentalPriceDTO.getTimeUnit(),"minute")){
            timeUnit = TimeUnit.MINUTES;
        }
        else{
            if(Objects.equals(rentalPriceDTO.getTimeUnit(),"day")){
                timeUnit = TimeUnit.DAYS;
            }
        }
        var value  = Double.parseDouble(rentalPriceDTO.getValue());
        var currency = Currency.getInstance(rentalPriceDTO.getCurrency());
        Optional<RentalPrice> databaseRentalPrice = rentalPriceRepository.findRentalPriceByValueAndCurrencyAndTimeUnit(value,currency,timeUnit);
        return databaseRentalPrice.orElse(new RentalPrice(value, Currency.getInstance(rentalPriceDTO.getCurrency()),timeUnit));
    }

    public RentalPriceDTO fromRentalPriceToDTO(RentalPrice rentalPrice){
        var timeUnit = "hour";
        if(Objects.equals(rentalPrice.getTimeUnit(),TimeUnit.MINUTES)) {
            timeUnit = "minute";
        }else{
            if(Objects.equals(rentalPrice.getTimeUnit(),TimeUnit.DAYS)){
                timeUnit = "day";
            }
        }
        var value = String.valueOf(rentalPrice.getValue());
        return new RentalPriceDTO(value,rentalPrice.getCurrency().getSymbol(),timeUnit);
    }

    public Vehicle fromDTOtoVehicle(VehicleDTO vehicleDTO) throws NumberFormatException{
        Optional<Company> company = companyRepository.findCompaniesByName(vehicleDTO.getRentalCompanyName());
        var rentalPrice = fromDTOtoRentalPrice(vehicleDTO.getRentalPriceDTO());
        rentalPriceRepository.save(rentalPrice);

        return company.map(value -> new Vehicle(vehicleDTO.getVin(),
                vehicleDTO.getRegistrationNumber(),
                vehicleDTO.getManufacturer(),
                vehicleDTO.getModel(),
                Integer.parseInt(vehicleDTO.getRangeLeftInKm()),
                Integer.parseInt(vehicleDTO.getYearOfManufacture()),
                Integer.parseInt(vehicleDTO.getHorsePower()),
                Integer.parseInt(vehicleDTO.getTorque()),
                Integer.parseInt(vehicleDTO.getMaximumAuthorisedMassInKg()),
                Integer.parseInt(vehicleDTO.getNumberOfSeats()),
                vehicleDTO.getLocation(),
                rentalPrice,
                value,
                vehicleDTO.isAvailable())).orElse(null);
    }

    public VehicleDTO fromVehicleToDTO(Vehicle vehicle){
        return new VehicleDTO(vehicle.getVin(),
                vehicle.getRegistrationNumber(),
                vehicle.getManufacturer(),
                vehicle.getModel(),
                String.valueOf(vehicle.getRangeLeftInKm()),
                String.valueOf(vehicle.getYearOfManufacture()),
                String.valueOf(vehicle.getHorsePower()),
                String.valueOf(vehicle.getTorque()),
                String.valueOf(vehicle.getMaximumAuthorisedMassInKg()),
                String.valueOf(vehicle.getNumberOfSeats()),
                String.valueOf(vehicle.getLocation()),
                fromRentalPriceToDTO(vehicle.getRentalPrice()),
                vehicle.getRentalCompany().getName(),
                vehicle.isAvailable());
    }
}

