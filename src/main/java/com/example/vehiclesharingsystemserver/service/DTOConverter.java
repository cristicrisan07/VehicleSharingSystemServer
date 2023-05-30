package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.*;
import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DTOConverter {

    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;
    private final RentalPriceRepository rentalPriceRepository;
    private final AccountRepository accountRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final RentalSessionRepository rentalSessionRepository;

    public DTOConverter(PasswordEncoder passwordEncoder, CompanyRepository companyRepository, RentalPriceRepository rentalPriceRepository, AccountRepository accountRepository, DriverRepository driverRepository, VehicleRepository vehicleRepository, RentalSessionRepository rentalSessionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.companyRepository = companyRepository;
        this.rentalPriceRepository = rentalPriceRepository;
        this.accountRepository = accountRepository;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.rentalSessionRepository = rentalSessionRepository;
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
        var value  = Double.parseDouble(rentalPriceDTO.getValue());
        var currency = Currency.getInstance(rentalPriceDTO.getCurrency());
        Optional<RentalPrice> databaseRentalPrice = rentalPriceRepository.findRentalPriceByValueAndCurrencyAndTimeUnit(value,currency,rentalPriceDTO.getTimeUnit());
        if(databaseRentalPrice.isPresent()){
            return databaseRentalPrice.get();
        }
        else{
            var rentalPrice = new RentalPrice(value, Currency.getInstance(rentalPriceDTO.getCurrency()), rentalPriceDTO.getTimeUnit());
            rentalPriceRepository.save(rentalPrice);
            return rentalPrice;
        }
    }

    public RentalPriceDTO fromRentalPriceToDTO(RentalPrice rentalPrice){
        var value = String.valueOf(rentalPrice.getValue());
        return new RentalPriceDTO(value,rentalPrice.getCurrency().getSymbol(),rentalPrice.getTimeUnit());
    }

    public Subscription fromDTOtoSubscription(SubscriptionDTO subscriptionDTO){
        var rentalPrice = fromDTOtoRentalPrice(subscriptionDTO.getRentalPriceDTO());
        return new Subscription(subscriptionDTO.getName(),rentalPrice,subscriptionDTO.getKilometersLimit());
    }

    public SubscriptionDTO fromSubscriptionToDTO(Subscription subscription){
        return new SubscriptionDTO(subscription.getId().toString(), subscription.getName(),
                subscription.getKilometersLimit(),
                fromRentalPriceToDTO(subscription.getRentalPrice()));
    }

    public Vehicle fromDTOtoVehicle(VehicleDTO vehicleDTO) throws NumberFormatException{
        Optional<Company> company = companyRepository.findCompaniesByName(vehicleDTO.getRentalCompanyName());
        var rentalPrice = fromDTOtoRentalPrice(vehicleDTO.getRentalPriceDTO());

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

    public ActiveSubscriptionDTO fromActiveSubscriptionToDTO(ActiveSubscription activeSubscription){
        return new ActiveSubscriptionDTO(activeSubscription.getId().toString(),
                fromSubscriptionToDTO(activeSubscription.getSubscription()),
                activeSubscription.getStartDate().toString(),
                activeSubscription.getEndDate().toString());
    }

    public RentalSession fromDTOtoRentalSession(RentalSessionDTO rentalSessionDTO) throws NoSuchElementException {
        var databaseAccount = accountRepository.findByUsername(rentalSessionDTO.getDriverUsername())
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_ACCOUNT")));
        var databaseDriver = driverRepository.findByAccount(databaseAccount)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_DRIVER")));
        var databaseVehicle = vehicleRepository.findVehicleByVin(rentalSessionDTO.getVehicleVIN())
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_VEHICLE")));
        var vehicleRentalSession = rentalSessionRepository.findRentalSessionByVehicleAndEndTime(databaseVehicle,null);
        if(vehicleRentalSession.isPresent()){
            return null;
        }
        else{
            return new RentalSession(databaseDriver,databaseVehicle, LocalDateTime.parse(rentalSessionDTO.getStartTime()));
        }
    }

    public CurrentRentalSessionDTO fromRentalSessionToCurrentRentalSessionDTO(RentalSession rentalSession){
        return new CurrentRentalSessionDTO(rentalSession.getId().toString(), fromVehicleToDTO(rentalSession.getVehicle()),
                rentalSession.getStartTime().toString(), rentalSession.getEndTime() == null ? null : rentalSession.getEndTime().toString(), rentalSession.getCost());
    }

    public VehiclePendingUpdate fromVehicleDTOtoVehiclePendingUpdate(VehicleDTO vehicleDTO)throws NumberFormatException{
        Optional<Company> company = companyRepository.findCompaniesByName(vehicleDTO.getRentalCompanyName());
        var rentalPrice = fromDTOtoRentalPrice(vehicleDTO.getRentalPriceDTO());

        return company.map(value -> new VehiclePendingUpdate(vehicleDTO.getVin(),
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

    public IdentityValidationDocumentDTO fromIdentityValidationDocumentToDTO(IdentityValidationDocument identityValidationDocument){
        var encoder = Base64.getEncoder();
        var photoFrontIVD = identityValidationDocument.getPhotos().stream().toList().get(0);
        var photoBackIVD = identityValidationDocument.getPhotos().stream().toList().get(1);
        try {
            var encodedByteArrayFront = encoder.encodeToString(photoFrontIVD.getPhoto().getBinaryStream().readAllBytes());
            var encodedByteArrayBack = encoder.encodeToString(photoBackIVD.getPhoto().getBinaryStream().readAllBytes());
            return new IdentityValidationDocumentDTO(
                    identityValidationDocument.getDriver().getAccount().getUsername(),
                    encodedByteArrayFront,
                    encodedByteArrayBack);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

