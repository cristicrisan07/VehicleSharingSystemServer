package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.Company;
import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.model.Vehicle;
import com.example.vehiclesharingsystemserver.repository.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RentalCompanyManagerOperationsService {
    private final RentalCompanyManagerRepository rentalCompanyManagerRepository;
    private final CompanyRepository companyRepository;
    private final VehicleRepository vehicleRepository;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final DTOConverter dtoConverter;

    public RentalCompanyManagerOperationsService(RentalCompanyManagerRepository rentalCompanyManagerRepository, CompanyRepository companyRepository, VehicleRepository vehicleRepository, AccountRepository accountRepository, AuthenticationManager authenticationManager, JwtService jwtService, DTOConverter dtoConverter) {
        this.rentalCompanyManagerRepository = rentalCompanyManagerRepository;
        this.companyRepository = companyRepository;
        this.vehicleRepository = vehicleRepository;
        this.accountRepository = accountRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.dtoConverter = dtoConverter;
    }

    public LoggedInManagerDTO authenticate(AccountDTO accountDTO) throws NoSuchElementException {
        var account = accountRepository.findByUsername(accountDTO.getUsername()).orElseThrow();
        if(Objects.equals(account.getAccountType(),"manager")){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountDTO.getUsername(),accountDTO.getPassword()));
            String jwtToken = jwtService.generateToken(new HashMap<>(),account);
            var manager = rentalCompanyManagerRepository.findRentalCompanyManagerByAccount(account);
            return manager.map(rentalCompanyManager -> new LoggedInManagerDTO(jwtToken, rentalCompanyManager.getCompany().getName())).orElse(null);
        }
        else{
            throw new NoSuchElementException();
        }

    }

    public String addVehicle(VehicleDTO vehicleDTO){
        var possibleVehicle = vehicleRepository.findVehicleByVin(vehicleDTO.getVin());
        if(possibleVehicle.isPresent()){
            return "ERROR: VIN_ALREADY_EXISTS";
        }else{
            if(vehicleRepository.findVehicleByRegistrationNumber(vehicleDTO.getRegistrationNumber()).isPresent()){
                return "ERROR: REGISTRATION_NUMBER_ALREADY_EXISTS";
            }
            else {
                try {

                    var vehicle = dtoConverter.fromDTOtoVehicle(vehicleDTO);
                    if (vehicle != null) {
                        vehicleRepository.save(vehicle);
                        return "SUCCESS";
                    } else {
                        return "ERROR: INCORRECT_COMPANY_NAME";
                    }
                } catch (NumberFormatException e) {
                    return "ERROR: CHECK_NUMERIC_FIELDS_FORMAT";
                }
            }
        }
    }

    public List<VehicleDTO> getVehiclesOfCompany(String companyName){
        Optional<Company> databaseCompany = companyRepository.findCompaniesByName(companyName);
        if(databaseCompany.isEmpty()){
            return null;
        }
        else {
            Iterable<Vehicle> iterableVehicles = vehicleRepository.findAllByRentalCompany(databaseCompany.get());
            ArrayList<Vehicle> vehicles = new ArrayList<>();
            iterableVehicles.forEach(vehicles::add);
            return vehicles.stream().map(dtoConverter::fromVehicleToDTO).toList();
        }
    }

    public String updateVehicle(VehicleDTO vehicleDTO){
        Optional<Vehicle> databaseVehicle = vehicleRepository.findVehicleByVin(vehicleDTO.getVin());
        if(databaseVehicle.isEmpty()){
            return "ERROR: COULD_NOT_FIND_VEHICLE";
        }else{
            if(!Objects.equals(vehicleDTO.getRegistrationNumber(),databaseVehicle.get().getRegistrationNumber()) &&
            vehicleRepository.findVehicleByRegistrationNumber(vehicleDTO.getRegistrationNumber()).isPresent()){
                return "ERROR: REGISTRATION_NUMBER_TAKEN";
            }else{
                databaseVehicle.get().setRegistrationNumber(vehicleDTO.getRegistrationNumber());
                databaseVehicle.get().setRangeLeftInKm(Integer.parseInt(vehicleDTO.getRangeLeftInKm()));
                databaseVehicle.get().setHorsePower(Integer.parseInt(vehicleDTO.getHorsePower()));
                databaseVehicle.get().setTorque(Integer.parseInt(vehicleDTO.getTorque()));
                databaseVehicle.get().setMaximumAuthorisedMassInKg(Integer.parseInt(vehicleDTO.getMaximumAuthorisedMassInKg()));
                databaseVehicle.get().setNumberOfSeats(Integer.parseInt(vehicleDTO.getNumberOfSeats()));
                databaseVehicle.get().setLocation(vehicleDTO.getLocation());
                databaseVehicle.get().setAvailable(vehicleDTO.isAvailable());
                databaseVehicle.get().setRentalPrice(dtoConverter.fromDTOtoRentalPrice(vehicleDTO.getRentalPriceDTO()));
                vehicleRepository.save(databaseVehicle.get());
                return "SUCCESS";
            }
        }
    }

    public String deleteVehicle(String vin){
        Optional<Vehicle> databaseVehicle = vehicleRepository.findVehicleByVin(vin);
        if(databaseVehicle.isEmpty()){
            return "ERROR: COULD_NOT_FIND_VEHICLE";
        }
        else{
            vehicleRepository.delete(databaseVehicle.get());
            return "SUCCESS";
        }
    }
}
