package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.*;
import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.repository.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final RentalSessionRepository rentalSessionRepository;
    private final DTOConverter dtoConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public DriverOperationsService(AccountRepository accountRepository, DriverRepository driverRepository, VehicleRepository vehicleRepository, SubscriptionRepository subscriptionRepository, PaymentRepository paymentRepository, CardRepository cardRepository, DTOConverter dtoConverter, AuthenticationManager authenticationManager, JwtService jwtService, ActiveSubscriptionRepository activeSubscriptionRepository, RentalSessionRepository rentalSessionRepository) {
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
        this.rentalSessionRepository = rentalSessionRepository;
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
    public List<VehicleDTO> getAllAvailableVehicles(){
        Iterable<Vehicle> iterableVehicles = vehicleRepository.findAll();
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        iterableVehicles.forEach(vehicles::add);
        return vehicles.stream().filter(vehicle -> rentalSessionRepository.findRentalSessionByVehicleAndAndEndTime(vehicle,null).isEmpty()).map(dtoConverter::fromVehicleToDTO).toList();
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
            var payment = new Payment(card,Double.parseDouble(subscriptionContractDTO.getValue()));
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

    public ActiveSubscriptionDTO getDriverSubscription(String username) throws NoSuchElementException{
        var databaseAccount = accountRepository.findByUsername(username).orElseThrow();
        var databaseDriver = driverRepository.findByAccount(databaseAccount).orElseThrow();
        var driverActiveSubscription = databaseDriver.getActiveSubscription();
        return driverActiveSubscription != null ? dtoConverter.fromActiveSubscriptionToDTO(driverActiveSubscription) : null;
    }

    public String startRentalSession(RentalSessionDTO rentalSessionDTO) throws Exception {
       var rentalSession =  dtoConverter.fromDTOtoRentalSession(rentalSessionDTO);
           if(rentalSession != null){
               rentalSessionRepository.save(rentalSession);
               return "SUCCESS";
           }
           else{
               return "VEHICLE_ALREADY_IN_USE";
           }
    }

    public String endRentalSession(RentalSessionDTO rentalSessionDTO){
        var databaseAccount = accountRepository.findByUsername(rentalSessionDTO.getDriverUsername())
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_ACCOUNT")));
        var databaseDriver = driverRepository.findByAccount(databaseAccount)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_DRIVER")));
        var vehicleRentalSession = rentalSessionRepository.findRentalSessionByDriver(databaseDriver)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_RENTAL_SESSION")));
        if(databaseDriver.getActiveSubscription() == null){
            return "EXPIRED_SUBSCRIPTION";
        }

        vehicleRentalSession.setEndTime(Timestamp.valueOf(rentalSessionDTO.getEndTime()));
        vehicleRentalSession.setCost(Double.parseDouble(rentalSessionDTO.getCost()));

        var payment = !Objects.equals(rentalSessionDTO.getEncryptedCardNumber(), "")
                ?
            new Payment(cardRepository.findCardByCardNumber(rentalSessionDTO.getEncryptedCardNumber())
                    .orElseGet(() -> {
                        Card card = new Card(rentalSessionDTO.getEncryptedCardNumber());
                        cardRepository.save(card);
                        return card;
                    }),Double.parseDouble(rentalSessionDTO.getCost()))
                :
                new Payment(databaseDriver.getActiveSubscription());

        paymentRepository.save(payment);
        vehicleRentalSession.setPayment(payment);

        return "SUCCESS";
    }
}
