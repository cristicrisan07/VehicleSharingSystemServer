package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.*;
import com.example.vehiclesharingsystemserver.model.DTO.*;
import com.example.vehiclesharingsystemserver.repository.*;
import io.netty.util.concurrent.UnaryPromiseNotifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.example.vehiclesharingsystemserver.service.EncryptionService.encrypt;
import static com.example.vehiclesharingsystemserver.service.VehicleOperationsService.getSuitablePort;

@Service
public class DriverOperationsService {

    private final AccountRepository accountRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final ActiveSubscriptionRepository activeSubscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final VehiclePendingUpdateRepository vehiclePendingUpdateRepository;
    private final IdentityValidationDocumentRepository identityValidationDocumentRepository;
    private final DocumentPhotoRepository documentPhotoRepository;
    private final CardRepository cardRepository;
    private final RentalSessionRepository rentalSessionRepository;
    private final DTOConverter dtoConverter;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public DriverOperationsService(AccountRepository accountRepository, DriverRepository driverRepository, VehicleRepository vehicleRepository, SubscriptionRepository subscriptionRepository, PaymentRepository paymentRepository, VehiclePendingUpdateRepository vehiclePendingUpdateRepository, IdentityValidationDocumentRepository identityValidationDocumentRepository, DocumentPhotoRepository documentPhotoRepository, CardRepository cardRepository, DTOConverter dtoConverter, AuthenticationManager authenticationManager, JwtService jwtService, ActiveSubscriptionRepository activeSubscriptionRepository, RentalSessionRepository rentalSessionRepository) {
        this.accountRepository = accountRepository;
        this.vehicleRepository = vehicleRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.paymentRepository = paymentRepository;
        this.vehiclePendingUpdateRepository = vehiclePendingUpdateRepository;
        this.identityValidationDocumentRepository = identityValidationDocumentRepository;
        this.documentPhotoRepository = documentPhotoRepository;
        this.cardRepository = cardRepository;
        this.dtoConverter = dtoConverter;
        this.driverRepository = driverRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.activeSubscriptionRepository = activeSubscriptionRepository;
        this.rentalSessionRepository = rentalSessionRepository;
    }

    public String register(UserDTO userDTO){
        String result = checkIfUsernameExists(userDTO.getAccount().getUsername());
        if(result.equals("DOESN'T EXIST")) {
            if (accountRepository.findByEmailAddress(userDTO.getAccount().getEmailAddress()).isPresent()) {
                return "ERROR: EMAIL_TAKEN";
            }
            if (accountRepository.findAccountByPhoneNumber(userDTO.getAccount().getPhoneNumber()).isPresent()) {
                return "ERROR: PHONE_NUMBER_TAKEN";
            }
            var driver = dtoConverter.fromUserDTOtoDriver(userDTO);
            accountRepository.save(driver.getAccount());
            driverRepository.save(driver);
            return jwtService.generateToken(new HashMap<>(), driver.getAccount());
        }else{
            return "ERROR: USERNAME_TAKEN";
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
        return vehicles.stream().filter(vehicle -> vehicle.isAvailable() && rentalSessionRepository.findRentalSessionByVehicleAndEndTime(vehicle,null).isEmpty()).map(dtoConverter::fromVehicleToDTO).toList();
    }
    public ActiveSubscriptionDTO addSubscriptionToDriver(SubscriptionContractDTO subscriptionContractDTO) throws NoSuchElementException{
        var databaseAccount = accountRepository.findByUsername(subscriptionContractDTO.getDriverUsername()).orElseThrow(()->new NoSuchElementException("DRIVER_NOT_FOUND"));
        var databaseDriver = driverRepository.findByAccount(databaseAccount).orElseThrow(()->new NoSuchElementException("DRIVER_NOT_FOUND"));
        var databaseSubscription = subscriptionRepository.findById(UUID.fromString(subscriptionContractDTO.getSubscriptionId()));
        if(databaseSubscription.isEmpty()){
            throw new NoSuchElementException("CANNOT_FIND_REQUESTED_SUBSCRIPTION");
        }else{

            var payment = new Payment(cardRepository.findCardByCardNumber(subscriptionContractDTO.getEncryptedCardNumber())
                            .orElseGet(() -> {
                                Card card = new Card(subscriptionContractDTO.getEncryptedCardNumber());
                                cardRepository.save(card);
                                return card;
                            }),Double.parseDouble(subscriptionContractDTO.getValue()));
            paymentRepository.save(payment);

            var now = LocalDateTime.now();
            var subscriptionDuration = 1;
            if(databaseSubscription.get().getRentalPrice().getTimeUnit().equals("week")){
                subscriptionDuration = 7;
            }
            else{
                if(databaseSubscription.get().getRentalPrice().getTimeUnit().equals("month")){
                    subscriptionDuration = 30;
                }
            }

            var activeSubscription = new ActiveSubscription(
                    databaseSubscription.get(),
                    now,
                    LocalDateTime.from(now.plus(subscriptionDuration,ChronoUnit.DAYS)),
                    payment);
            activeSubscriptionRepository.save(activeSubscription);
            databaseDriver.setActiveSubscription(activeSubscription);
            driverRepository.save(databaseDriver);
            return dtoConverter.fromActiveSubscriptionToDTO(activeSubscription);
        }
    }

    public ActiveSubscriptionDTO getDriverSubscription(String username) throws NoSuchElementException{
        var databaseAccount = accountRepository.findByUsername(username).orElseThrow();
        var databaseDriver = driverRepository.findByAccount(databaseAccount).orElseThrow();
        var driverActiveSubscription = databaseDriver.getActiveSubscription();
        String message = "NO_SUBSCRIPTION";
        if(driverActiveSubscription != null) {
            if(driverActiveSubscription.getEndDate().isBefore(LocalDateTime.now())){
                databaseDriver.setActiveSubscription(null);
                message = "SUBSCRIPTION_EXPIRED";
            }else {
               return dtoConverter.fromActiveSubscriptionToDTO(driverActiveSubscription);
            }
        }

        return new ActiveSubscriptionDTO(
                message,
                new SubscriptionDTO(
                        "",
                        "",
                        0,
                        new RentalPriceDTO("","","")),
                "",
                "");
    }

    public String startRentalSession(RentalSessionDTO rentalSessionDTO)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException,
            NoSuchPaddingException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        var account = accountRepository.findByUsername(rentalSessionDTO.getDriverUsername()).orElseThrow();
        var rentalSession = dtoConverter.fromDTOtoRentalSession(rentalSessionDTO);
           if (rentalSession != null) {
               rentalSessionRepository.save(rentalSession);
                   var vehicleReceivedCommandStatus =
                           sendRentalSessionTokenToVehicleController(
                                   encrypt(rentalSession.getId().toString()),rentalSession.getVehicle().getVin());
                   if(vehicleReceivedCommandStatus.equals("SUCCESS")) {
                       return "SUCCESS:" + rentalSession.getId().toString();
                   }
                    else{
                        rentalSessionRepository.delete(rentalSession);
                       return "CANNOT_REACH_VEHICLE_CONTROLLER";
                   }

           } else {
               return "VEHICLE_ALREADY_IN_USE";
           }


    }



    private String sendRentalSessionTokenToVehicleController(String token,String vin){
        WebClient client = WebClient.create();
        try {
            String suitablePort = getSuitablePort(vin);
            if(suitablePort!=null) {
                return client.post()
                        .uri(new URI("http://localhost:"+getSuitablePort(vin)+"/vehicleControllerSimulator/controller/allowConnection"))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .bodyValue(token)
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

    private String sendConnectionClosingSignalToController(String vin){
        WebClient client = WebClient.create();
        try {
            String suitablePort = getSuitablePort(vin);
            if(suitablePort!=null) {
                return client.delete()
                        .uri(new URI("http://localhost:"+suitablePort+"/vehicleControllerSimulator/controller/denyConnection"))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
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

    public String endRentalSession(RentalSessionDTO rentalSessionDTO) throws NoSuchElementException{
        var databaseAccount = accountRepository.findByUsername(rentalSessionDTO.getDriverUsername())
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_ACCOUNT")));
        var databaseDriver = driverRepository.findByAccount(databaseAccount)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_DRIVER")));
        var vehicleRentalSession = rentalSessionRepository.findRentalSessionByDriverAndEndTime(databaseDriver,null)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_RENTAL_SESSION")));

        String responseFromController = sendConnectionClosingSignalToController(vehicleRentalSession.getVehicle().getVin());
        if(responseFromController.equals("SUCCESS")) {
            vehicleRentalSession.setEndTime(LocalDateTime.parse(rentalSessionDTO.getEndTime()));
            vehicleRentalSession.setCost(Double.parseDouble(rentalSessionDTO.getCost()));
            var vehicle = vehicleRentalSession.getVehicle();
            vehicle.setLocation(rentalSessionDTO.getLocation());

            var possiblePendingUpdate = vehiclePendingUpdateRepository.findVehiclePendingUpdateByVin(vehicle.getVin());
            if (possiblePendingUpdate.isPresent()) {
                var pendingVehicleChangeValue = possiblePendingUpdate.get();
                var availabilityValue = vehicle.getEmergencyInterventions().stream()
                        .noneMatch(obj -> obj.getAction().equals("LIMP_MODE") && obj.getStatus().equals("ISSUED"))
                        && pendingVehicleChangeValue.isAvailable();
                vehicle.setVin(pendingVehicleChangeValue.getVin());
                vehicle.setRegistrationNumber(pendingVehicleChangeValue.getRegistrationNumber());
                vehicle.setManufacturer(pendingVehicleChangeValue.getManufacturer());
                vehicle.setModel(pendingVehicleChangeValue.getModel());
                vehicle.setRangeLeftInKm(pendingVehicleChangeValue.getRangeLeftInKm());
                vehicle.setYearOfManufacture(pendingVehicleChangeValue.getYearOfManufacture());
                vehicle.setHorsePower(pendingVehicleChangeValue.getHorsePower());
                vehicle.setTorque(pendingVehicleChangeValue.getTorque());
                vehicle.setMaximumAuthorisedMassInKg(pendingVehicleChangeValue.getMaximumAuthorisedMassInKg());
                vehicle.setNumberOfSeats(pendingVehicleChangeValue.getNumberOfSeats());
                vehicle.setRentalPrice(pendingVehicleChangeValue.getRentalPrice());
                vehicle.setRentalCompany(pendingVehicleChangeValue.getRentalCompany());
                vehicle.setAvailable(availabilityValue);
                vehicleRepository.save(vehicle);
                vehiclePendingUpdateRepository.delete(pendingVehicleChangeValue);
            }

            rentalSessionRepository.save(vehicleRentalSession);
            return "SUCCESS";
        }else{
            return responseFromController;
        }
    }

    public String payForRentalSession(PaymentDTO paymentDTO) throws NoSuchElementException{
        var vehicleRentalSession = rentalSessionRepository.findById(UUID.fromString(paymentDTO.getRentalSessionId()))
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_RENTAL_SESSION")));
        var driverActiveSubscription = vehicleRentalSession.getDriver().getActiveSubscription();
        if(Objects.equals(paymentDTO.getEncryptedCardNumber(), "")) {
            if (driverActiveSubscription != null) {
                if (driverActiveSubscription.getEndDate().isBefore(LocalDateTime.now())) {
                    vehicleRentalSession.getDriver().setActiveSubscription(null);
                    return "EXPIRED_SUBSCRIPTION";
                }
            }else {
                return "EXPIRED_SUBSCRIPTION";
            }
        }

        var payment = !Objects.equals(paymentDTO.getEncryptedCardNumber(), "")
                ?
                new Payment(cardRepository.findCardByCardNumber(paymentDTO.getEncryptedCardNumber())
                        .orElseGet(() -> {
                            Card card = new Card(paymentDTO.getEncryptedCardNumber());
                            cardRepository.save(card);
                            return card;
                        }),Double.parseDouble(paymentDTO.getCost()))
                :
                new Payment(vehicleRentalSession.getDriver().getActiveSubscription());

        paymentRepository.save(payment);
        vehicleRentalSession.setPayment(payment);
        rentalSessionRepository.save(vehicleRentalSession);
        return "SUCCESS";

    }

    public String updateRentalSession(RentalSessionDTO rentalSessionDTO){
        var databaseVehicle = vehicleRepository.findVehicleByVin(rentalSessionDTO.getVehicleVIN())
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_VEHICLE")));
        var vehicleRentalSession = rentalSessionRepository.findRentalSessionByVehicle(databaseVehicle)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_RENTAL_SESSION")));
        databaseVehicle.setLocation(rentalSessionDTO.getLocation());
        vehicleRentalSession.setCost(Double.parseDouble(rentalSessionDTO.getCost()));
        return "SUCCESS";
    }

    public CurrentRentalSessionDTO getCurrentRentalSession(String username){
        var databaseAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_ACCOUNT")));
        var databaseDriver = driverRepository.findByAccount(databaseAccount)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_DRIVER")));
        var vehicleRentalSession = rentalSessionRepository
                .findRentalSessionByDriverAndPayment(databaseDriver,null)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_RENTAL_SESSION")));
        return dtoConverter.fromRentalSessionToCurrentRentalSessionDTO(vehicleRentalSession);
    }

    public String setDrivingLicensePhoto(IdentityValidationDocumentDTO identityValidationDocumentDTO) throws SQLException  {
        var databaseAccount = accountRepository.findByUsername(identityValidationDocumentDTO.getUsername())
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_ACCOUNT")));
        var databaseDriver = driverRepository.findByAccount(databaseAccount)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_DRIVER")));
        var decoder = Base64.getMimeDecoder();
        var photoFrontIVD = identityValidationDocumentDTO.getPhotoFront();
        var photoBackIVD = identityValidationDocumentDTO.getPhotoBack();
        var photoIDIVD = identityValidationDocumentDTO.getPhotoIDCard();
        var decodedByteArrayFront = decoder.decode(photoFrontIVD);
        var decodedByteArrayBack = decoder.decode(photoBackIVD);
        var decodedByteArrayID = decoder.decode(photoIDIVD);
        var inputStreamFront = new ByteArrayInputStream(decodedByteArrayFront);
        var inputStreamBack = new ByteArrayInputStream(decodedByteArrayBack);
        var inputStreamID = new ByteArrayInputStream(decodedByteArrayID);
        Blob photoFront = new SerialBlob(inputStreamFront.readAllBytes());
        Blob photoBack = new SerialBlob(inputStreamBack.readAllBytes());
        Blob photoID = new SerialBlob(inputStreamID.readAllBytes());

        var identityValidationDocument = identityValidationDocumentRepository.findByDriver(databaseDriver)
                .orElse(new IdentityValidationDocument("driving_license","PENDING_VALIDATION",databaseDriver));
        if(identityValidationDocument.getStatus().equals("INVALID")){
            var iterableDocumentPhotos = documentPhotoRepository.findByIdentityValidationDocument(identityValidationDocument);
            documentPhotoRepository.deleteAll(iterableDocumentPhotos);
            identityValidationDocument.setStatus("PENDING_VALIDATION");
        }
        var documentPhotoFront = new DocumentPhoto(photoFront,identityValidationDocument);
        var documentPhotoBack = new DocumentPhoto(photoBack,identityValidationDocument);
        var documentPhotoID = new DocumentPhoto(photoID,identityValidationDocument);
        identityValidationDocumentRepository.save(identityValidationDocument);
        documentPhotoRepository.save(documentPhotoFront);
        documentPhotoRepository.save(documentPhotoBack);
        documentPhotoRepository.save(documentPhotoID);
        return "SUCCESS";
    }

    public String hasSubmittedDocuments(String username) {
        var databaseAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_ACCOUNT")));
        var databaseDriver = driverRepository.findByAccount(databaseAccount)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_DRIVER")));
        if (identityValidationDocumentRepository.findByDriver(databaseDriver).isPresent()) {
            return "SUBMITTED";
        } else {
            return "NOT_SUBMITTED";
        }
    }

    public String isValidatedAsLegalDriver(String username){
        var databaseAccount = accountRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_ACCOUNT")));
        var databaseDriver = driverRepository.findByAccount(databaseAccount)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_DRIVER")));
        var databaseIdentityValidationDocument = identityValidationDocumentRepository.findByDriver(databaseDriver)
                .orElseThrow(()-> new NoSuchElementException(("NO_SUCH_DOCUMENT")));
        return databaseIdentityValidationDocument.getStatus();
    }
}
