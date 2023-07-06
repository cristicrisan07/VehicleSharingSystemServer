package com.example.vehiclesharingsystemserver.service;

import com.example.vehiclesharingsystemserver.controller.AdministratorController;
import com.example.vehiclesharingsystemserver.controller.DriverController;
import com.example.vehiclesharingsystemserver.controller.ManagerController;
import com.example.vehiclesharingsystemserver.model.DTO.VehicleControllerLocationDTO;
import com.example.vehiclesharingsystemserver.model.DTO.VehicleDTO;
import com.example.vehiclesharingsystemserver.model.Vehicle;
import com.example.vehiclesharingsystemserver.repository.EmergencyInterventionRepository;
import com.example.vehiclesharingsystemserver.repository.VehiclePendingUpdateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VehicleOperationsServiceTest {

    @MockBean
    private JwtService jwtService;
    @MockBean
    private AdministratorController administratorController;
    @Autowired
    private VehicleOperationsService vehicleOperationsService;
    @Autowired
    private VehiclePendingUpdateRepository vehiclePendingUpdateRepository;
    @Autowired
    private EmergencyInterventionRepository emergencyInterventionRepository;
    @Autowired
    private DTOConverter dtoConverter;
    @Autowired
    private RentalCompanyManagerOperationsService rentalCompanyManagerOperationsService;
    @MockBean
    private DriverController driverController;
    @MockBean
    private ManagerController managerController;

    @Test
    void invalidVINThrowsException() {
        VehicleControllerLocationDTO dto = new VehicleControllerLocationDTO("NON-EXISTENT_VIN", "location", "time");
        assertThrows(NoSuchElementException.class, ()-> vehicleOperationsService.setCurrentLocation(dto));
    }

    private boolean equalVehicleData(VehicleDTO one, VehicleDTO two){
        var fieldsOne = new HashMap<String,Object>();
        var fieldsTwo = new HashMap<String,Object>();
        Stream<Field> fieldsOneStream = Arrays.stream(one.getClass().getDeclaredFields());
        fieldsOneStream.forEach(field -> {
            try {
                fieldsOne.put(field.getName(),field.get(one));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fieldsOne.put(field.getName(),null);
            }
        });
        Stream<Field> fieldsTwoStream = Arrays.stream(two.getClass().getDeclaredFields());
        fieldsTwoStream.forEach(field -> {
            try {
                fieldsTwo.put(field.getName(),field.get(two));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                fieldsTwo.put(field.getName(),null);
            }
        });

        AtomicBoolean haveEqualData = new AtomicBoolean(true);
        fieldsOne.forEach((key,value) -> {
                if(value == fieldsTwo.get(key)){
                    haveEqualData.set(false);
                }
            });
        return haveEqualData.get();
    }
    @Test
    void updatedVehicleDataIsReturned(){
        var exampleCompany = "aglet laces s.r.l.";
        var pendingVehicleChange = vehiclePendingUpdateRepository.findAll().iterator().hasNext();
        if(pendingVehicleChange){
            var veh = vehiclePendingUpdateRepository.findAll().iterator().next();
            var vehDTO = dtoConverter.fromVehicleToDTO(veh);
            var companyVehicles = rentalCompanyManagerOperationsService.getVehiclesOfCompany(exampleCompany);
            assertTrue(companyVehicles.stream().anyMatch(vehicleDTO -> equalVehicleData(vehDTO, vehicleDTO)));
        }
    }
}