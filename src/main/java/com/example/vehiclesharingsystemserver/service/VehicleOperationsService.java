package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.model.DTO.VehicleControllerLocationDTO;
import com.example.vehiclesharingsystemserver.repository.RentalSessionRepository;
import com.example.vehiclesharingsystemserver.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.NoSuchElementException;

import static com.example.vehiclesharingsystemserver.service.EncryptionService.encrypt;

@Service
public class VehicleOperationsService {
    private final VehicleRepository vehicleRepository;
    private final RentalSessionRepository rentalSessionRepository;

    public VehicleOperationsService(VehicleRepository vehicleRepository, RentalSessionRepository rentalSessionRepository, JwtService jwtService) {
        this.vehicleRepository = vehicleRepository;
        this.rentalSessionRepository = rentalSessionRepository;
    }

    public String getCurrentRentalSession(String vin) throws NoSuchElementException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        var databaseVehicle = vehicleRepository.findVehicleByVin(vin)
                .orElseThrow(() -> new NoSuchElementException(("NO_SUCH_VEHICLE")));
        var vehicleRentalSession = rentalSessionRepository
                .findRentalSessionByVehicleAndEndTime(databaseVehicle,null);
        return vehicleRentalSession.isPresent()
                ?
                encrypt(vehicleRentalSession.get().getId().toString())
                :
                "NOT_RENTED" ;
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
}
