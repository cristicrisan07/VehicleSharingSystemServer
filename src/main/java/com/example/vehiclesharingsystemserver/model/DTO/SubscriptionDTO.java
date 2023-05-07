package com.example.vehiclesharingsystemserver.model.DTO;

public class SubscriptionDTO {

    private final String id;
    private final String name;
    private final int kilometersLimit;
    private final RentalPriceDTO rentalPriceDTO;

    public SubscriptionDTO(String id, String name, int kilometersLimit, RentalPriceDTO rentalPriceDTO) {
        this.id = id;
        this.name = name;
        this.kilometersLimit = kilometersLimit;
        this.rentalPriceDTO = rentalPriceDTO;
    }

    public String getName() {
        return name;
    }

    public int getKilometersLimit() {
        return kilometersLimit;
    }

    public RentalPriceDTO getRentalPriceDTO() {
        return rentalPriceDTO;
    }
    public String getId() {
        return id;
    }
}
