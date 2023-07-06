package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.DTO.EmergencyActionDTOForController;
import com.example.vehiclesharingsystemserver.model.DTO.VehicleControllerLocationDTO;
import com.example.vehiclesharingsystemserver.model.DTO.VehicleControllerStateDTO;
import com.example.vehiclesharingsystemserver.repository.RentalSessionRepository;
import com.example.vehiclesharingsystemserver.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.example.vehiclesharingsystemserver.service.EncryptionService.encrypt;

@Service
public class VehicleOperationsService {
    private final VehicleRepository vehicleRepository;
    private final RentalSessionRepository rentalSessionRepository;
    private final DTOConverter dtoConverter;

    public VehicleOperationsService(VehicleRepository vehicleRepository, RentalSessionRepository rentalSessionRepository, DTOConverter dtoConverter) {
        this.vehicleRepository = vehicleRepository;
        this.rentalSessionRepository = rentalSessionRepository;
        this.dtoConverter = dtoConverter;
    }


    public static String getSuitablePort(String vin){
        if(Objects.equals(vin, "4Y1SL65848Z411422")){
            return "8081";
        }else{
            if(Objects.equals(vin, "4Y1SL65848Z411439")){
                return "8082";
            }
        }
        return null;
    }

    public VehicleControllerStateDTO getCurrentRentalSession(String vin) throws NoSuchElementException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        var databaseVehicle = vehicleRepository.findVehicleByVin(vin)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_VEHICLE")));
        var vehicleRentalSession = rentalSessionRepository
                .findRentalSessionByVehicleAndEndTime(databaseVehicle,null);

        return new VehicleControllerStateDTO(vehicleRentalSession.isPresent()
                ? encrypt(vehicleRentalSession.get().getId().toString())
                : "NOT_RENTED",
                getEmergencyInterventionStatusDTOForController(vin));

    }

    public String setCurrentLocation(VehicleControllerLocationDTO vehicleControllerLocationDTO){
        var databaseVehicle = vehicleRepository.findVehicleByVin(vehicleControllerLocationDTO.getVin())
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_VEHICLE")));
        var databaseRentalSession = rentalSessionRepository.findRentalSessionByVehicleAndEndTime(databaseVehicle,null);
        if(databaseRentalSession.isPresent()) {
            databaseVehicle.setLocation(vehicleControllerLocationDTO.getLocation());
            databaseVehicle.setLastLocationReading(LocalDateTime.parse(vehicleControllerLocationDTO.getTime()));
            vehicleRepository.save(databaseVehicle);
            return "SUCCESS";
        }else{
            return "NOT_RENTED";
        }
    }

    public String getCurrentLocation(String vin){
        var databaseVehicle = vehicleRepository.findVehicleByVin(vin)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_VEHICLE")));
        return databaseVehicle.getLocation();
    }

    public EmergencyActionDTOForController getEmergencyInterventionStatusDTOForController(String vin){
        var databaseVehicle = vehicleRepository.findVehicleByVin(vin)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_VEHICLE")));
        return databaseVehicle.getEmergencyInterventions().stream()
                .filter(obj->obj.getAction().equals("LIMP_MODE") && obj.getStatus().equals("ISSUED"))
                .findFirst().map(dtoConverter::fromEmergencyToControllerDTO).orElse(new EmergencyActionDTOForController("NONE","",""));

    }
}
