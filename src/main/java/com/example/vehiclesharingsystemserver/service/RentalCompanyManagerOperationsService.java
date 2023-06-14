package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.Company;
import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.model.EmergencyIntervention;
import com.example.vehiclesharingsystemserver.model.Vehicle;
import com.example.vehiclesharingsystemserver.repository.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.vehiclesharingsystemserver.service.VehicleOperationsService.getSuitablePort;

@Service
public class RentalCompanyManagerOperationsService {
    private final RentalCompanyManagerRepository rentalCompanyManagerRepository;
    private final CompanyRepository companyRepository;
    private final VehicleRepository vehicleRepository;
    private final VehiclePendingUpdateRepository vehiclePendingUpdateRepository;
    private final EmergencyInterventionRepository emergencyInterventionRepository;
    private final AccountRepository accountRepository;
    private final RentalSessionRepository rentalSessionRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final DTOConverter dtoConverter;

    public RentalCompanyManagerOperationsService(RentalCompanyManagerRepository rentalCompanyManagerRepository, CompanyRepository companyRepository, VehicleRepository vehicleRepository, VehiclePendingUpdateRepository vehiclePendingUpdateRepository, EmergencyInterventionRepository emergencyInterventionRepository, AccountRepository accountRepository, RentalSessionRepository rentalSessionRepository, AuthenticationManager authenticationManager, JwtService jwtService, DTOConverter dtoConverter) {
        this.rentalCompanyManagerRepository = rentalCompanyManagerRepository;
        this.companyRepository = companyRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehiclePendingUpdateRepository = vehiclePendingUpdateRepository;
        this.emergencyInterventionRepository = emergencyInterventionRepository;
        this.accountRepository = accountRepository;
        this.rentalSessionRepository = rentalSessionRepository;
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
            iterableVehicles.forEach(vehicle->{
                var pendingVehicleChange = vehiclePendingUpdateRepository.findVehiclePendingUpdateByVin(vehicle.getVin());
                if(pendingVehicleChange.isPresent()){
                    var pendingVehicleChangeValue = pendingVehicleChange.get();
                    var availabilityValue = vehicle.getEmergencyInterventions().stream()
                            .noneMatch(obj -> obj.getAction().equals("LIMP_MODE") && obj.getStatus().equals("ISSUED"))
                            && pendingVehicleChangeValue.isAvailable();
                    vehicles.add(new Vehicle(pendingVehicleChangeValue.getVin(),
                            pendingVehicleChangeValue.getRegistrationNumber(),
                            pendingVehicleChangeValue.getManufacturer(),
                            pendingVehicleChangeValue.getModel(),
                            pendingVehicleChangeValue.getRangeLeftInKm(),
                            pendingVehicleChangeValue.getYearOfManufacture(),
                            pendingVehicleChangeValue.getHorsePower(),
                            pendingVehicleChangeValue.getTorque(),
                            pendingVehicleChangeValue.getMaximumAuthorisedMassInKg(),
                            pendingVehicleChangeValue.getNumberOfSeats(),
                            pendingVehicleChangeValue.getLocation(),
                            pendingVehicleChangeValue.getRentalPrice(),
                            pendingVehicleChangeValue.getRentalCompany(),
                            availabilityValue));
                }else{
                    vehicles.add(vehicle);
                }
            });
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
                String emergencyInterventionSolvedMessage = "";
                var emergency_actions = databaseVehicle.get().getEmergencyInterventions();
                if(vehicleDTO.isAvailable() &&
                        emergency_actions.stream().anyMatch
                                (obj->obj.getAction().equals("LIMP_MODE") && obj.getStatus().equals("ISSUED"))){

                    var solvedTime = LocalDateTime.now();
                    var controllerResponse = sendEmergencyActionToController(
                            new EmergencyIntervention(
                                    databaseVehicle.get(),
                                    solvedTime,
                                    null,
                                    "NORMAL",
                                    "SOLVED",
                                    "SOLVED"));
                    if(Objects.equals(controllerResponse,"SUCCESS")) {
                        databaseVehicle.get().setAvailable(vehicleDTO.isAvailable());
                        emergency_actions.stream()
                                .filter(it -> it.getAction().equals("LIMP_MODE") && it.getStatus().equals("ISSUED"))
                                .forEach(it -> {
                                    it.setStatus("SOLVED");
                                    it.setSolution_time(solvedTime);
                                    emergencyInterventionRepository.save(it);
                                });

                        vehicleRepository.save(databaseVehicle.get());
                        emergencyInterventionSolvedMessage = "ISSUE MARKED AS SOLVED";
                    }else{
                        return controllerResponse;
                    }
                }
                var databaseUnfinishedRentalSession = rentalSessionRepository.findRentalSessionByVehicleAndEndTime(databaseVehicle.get(),null);
                if(databaseUnfinishedRentalSession.isEmpty()) {
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
                    return "SUCCESS"+emergencyInterventionSolvedMessage;
                }else{
                    vehiclePendingUpdateRepository.findVehiclePendingUpdateByVin(vehicleDTO.getVin())
                            .ifPresent(vehiclePendingUpdateRepository::delete);
                    vehiclePendingUpdateRepository.save(dtoConverter.fromVehicleDTOtoVehiclePendingUpdate(vehicleDTO));
                    return "VEHICLE CURRENTLY IN USE. UPDATES WILL BE APPLIED AFTER THE END OF THE RENTAL SESSION."
                            +emergencyInterventionSolvedMessage;
                           // "LOCATION WON'T BE UPDATED";
                }
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

    public String getVehicleLocation(String vin){
        Optional<Vehicle> databaseVehicle = vehicleRepository.findVehicleByVin(vin);
        if(databaseVehicle.isEmpty()){
            return "ERROR: COULD_NOT_FIND_VEHICLE";
        }
        else{
            return databaseVehicle.get().getLocation();
        }
    }

    private String sendEmergencyActionToController(EmergencyIntervention emergencyIntervention){
        WebClient client = WebClient.create();
        try {
            String suitablePort = getSuitablePort(emergencyIntervention.getVehicle().getVin());
            if(suitablePort!=null) {
                return client.post()
                        .uri(new URI(
                                "http://localhost:"+
                                        suitablePort+
                                        "/vehicleControllerSimulator/controller/performEmergencyAction"))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(new EmergencyActionDTOForController(
                                emergencyIntervention.getAction(),
                                emergencyIntervention.getReason(),
                                LocalDateTime.now().toString()))
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
            }else{
                return "CONTROLLER_UNREACHABLE";
            }
        } catch (Exception e) {

            return "CANNOT_REACH_VEHICLE_CONTROLLER";
        }
    }

    public String createEmergencyIntervention(EmergencyActionDTOForWeb emergencyActionDTOForWeb){
        var account = accountRepository.findByUsername(emergencyActionDTOForWeb.getUsername())
                .orElseThrow(()->new NoSuchElementException("MANAGER_NOT_FOUND"));
        var databaseManager = rentalCompanyManagerRepository.findRentalCompanyManagerByAccount(account)
                .orElseThrow(()->new NoSuchElementException("MANAGER_NOT_FOUND"));
        var databaseVehicle = vehicleRepository.findVehicleByVin(emergencyActionDTOForWeb.getVin())
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_VEHICLE")));
        if(Objects.equals(emergencyActionDTOForWeb.getAction(),"LIMP_MODE")){
            var emergencyIntervention = new EmergencyIntervention
                    (databaseVehicle,
                    LocalDateTime.now(),
                    databaseManager,
                    emergencyActionDTOForWeb.getAction(),
                    "SUSPICIOUS_ACTIVITY",
                    "ISSUED");
            String response = sendEmergencyActionToController(emergencyIntervention);
            if(Objects.equals(response,"SUCCESS")) {
                emergencyInterventionRepository.save(emergencyIntervention);
                databaseVehicle.setAvailable(false);
                vehicleRepository.save(databaseVehicle);
                return "SUCCESS";
            }else{
                return response;
            }
        }else{
            return "INVALID_ACTION";
        }
    }

    public String getEmergencyInterventionStatus(String vin){
        var databaseVehicle = vehicleRepository.findVehicleByVin(vin)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_VEHICLE")));
        return databaseVehicle.getEmergencyInterventions().stream()
                .anyMatch(obj->obj.getAction().equals("LIMP_MODE") && obj.getStatus().equals("ISSUED"))
                ? "LIMP_MODE"
                : "NONE";
    }
}
