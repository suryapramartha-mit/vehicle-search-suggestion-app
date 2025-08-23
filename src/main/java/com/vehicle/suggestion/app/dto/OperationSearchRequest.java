package com.vehicle.suggestion.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationSearchRequest {
    private String brand;
    private String model;
    private String engine;
    private Integer yearStart;
    private Integer yearEnd;
    private Double distanceStart;
    private Double distanceEnd;
}
