package com.rca.parksafe.dto;

import lombok.Data;

@Data
public class CarResponse {
    private Long id;
    private String plateNumber;
    private boolean parked;
    private String parkedAt;
    private String driverUsername;
    private String driverEmail;
}