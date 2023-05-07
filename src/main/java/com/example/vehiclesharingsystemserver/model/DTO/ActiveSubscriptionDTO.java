package com.example.vehiclesharingsystemserver.model.DTO;

public class ActiveSubscriptionDTO {
    private final String id;
    private final SubscriptionDTO subscriptionDTO;
    private final String startDate;
    private final String endDate;

    public ActiveSubscriptionDTO(String id, SubscriptionDTO subscriptionDTO, String startDate, String endDate) {
        this.id = id;
        this.subscriptionDTO = subscriptionDTO;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public SubscriptionDTO getSubscriptionDTO() {
        return subscriptionDTO;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
