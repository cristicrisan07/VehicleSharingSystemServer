package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.ActiveSubscription;
import com.example.vehiclesharingsystemserver.model.Card;
import com.example.vehiclesharingsystemserver.model.DTO.AccountDTO;
import com.example.vehiclesharingsystemserver.model.DTO.SubscriptionContractDTO;
import com.example.vehiclesharingsystemserver.model.DTO.UserDTO;
import com.example.vehiclesharingsystemserver.model.DTO.VehicleDTO;
import com.example.vehiclesharingsystemserver.model.Payment;
import com.example.vehiclesharingsystemserver.model.Vehicle;
import com.example.vehiclesharingsystemserver.repository.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class DriverOperationsService {

    private final AccountRepository accountRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ActiveSubscriptionRepository activeSubscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final CardRepository cardRepository;
    private final DTOConverter dtoConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public DriverOperationsService(AccountRepository accountRepository, DriverRepository driverRepository, VehicleRepository vehicleRepository, SubscriptionRepository subscriptionRepository, PaymentRepository paymentRepository, CardRepository cardRepository, DTOConverter dtoConverter, AuthenticationManager authenticationManager, JwtService jwtService, ActiveSubscriptionRepository activeSubscriptionRepository) {
        this.accountRepository = accountRepository;
        this.vehicleRepository = vehicleRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentRepository = paymentRepository;
        this.cardRepository = cardRepository;
        this.dtoConverter = dtoConverter;
        this.driverRepository = driverRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.activeSubscriptionRepository = activeSubscriptionRepository;
    }

    public String register(UserDTO userDTO){

         String status = checkIfUsernameExists(userDTO.getAccount().getUsername());
         if(Objects.equals(status, "DOESN'T EXIST")) {
             if (accountRepository.findByEmailAddress(userDTO.getAccount().getEmailAddress()).isPresent()) {
                 return "EMAIL_TAKEN";
             }
             var driver = dtoConverter.fromUserDTOtoDriver(userDTO);
             accountRepository.save(driver.getAccount());
             driverRepository.save(driver);
             return jwtService.generateToken(new HashMap<>(), driver.getAccount());
         }
         else {
             return "ERROR: " + status;
         }

    }

    public String checkIfUsernameExists(String username) {
        if (accountRepository.findByUsername(username).isPresent()) {
            return "USERNAME_TAKEN";
        } else {
            return "DOESN'T EXIST";
        }
    }

    public String authenticate(AccountDTO accountDTO) throws NoSuchElementException{
        var account = accountRepository.findByUsername(accountDTO.getUsername()).orElseThrow();
        if(Objects.equals(account.getAccountType(),"driver")) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(accountDTO.getUsername(), accountDTO.getPassword()));
            return jwtService.generateToken(new HashMap<>(), account);
        }
        else{
            throw new NoSuchElementException();
        }
    }
    public List<VehicleDTO> getAllVehicles(){
        Iterable<Vehicle> iterableVehicles = vehicleRepository.findAll();
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        iterableVehicles.forEach(vehicles::add);
        return vehicles.stream().map(dtoConverter::fromVehicleToDTO).toList();
    }
    public String addSubscriptionToDriver(SubscriptionContractDTO subscriptionContractDTO) throws NoSuchElementException{
        var databaseAccount = accountRepository.findByUsername(subscriptionContractDTO.getDriverUsername()).orElseThrow();
        var databaseDriver = driverRepository.findByAccount(databaseAccount).orElseThrow();
        var databaseSubscription = subscriptionRepository.findById(UUID.fromString(subscriptionContractDTO.getSubscriptionId()));
        if(databaseSubscription.isEmpty()){
            return "CANNOT_FIND_SUBSCRIPTION";
        }else{
            var card = new Card(subscriptionContractDTO.getEncryptedCardNumber());
            cardRepository.save(card);
            var payment = new Payment(card);
            paymentRepository.save(payment);

            var instant = Instant.now();
            var subscriptionDuration = 1;
            if(databaseSubscription.get().getRentalPrice().getTimeUnit().equals("week")){
                subscriptionDuration = 7;
            }
            else{
                if(databaseSubscription.get().getRentalPrice().getTimeUnit().equals("month")){
                    subscriptionDuration = 30;
                }
            }

            var activeSubscription = new ActiveSubscription(databaseSubscription.get(),Timestamp.from(instant),Timestamp.from(instant.plus(subscriptionDuration,ChronoUnit.DAYS)),payment);
            activeSubscriptionRepository.save(activeSubscription);
            databaseDriver.setActiveSubscription(activeSubscription);
            return "SUCCESS";
        }

    }
}
