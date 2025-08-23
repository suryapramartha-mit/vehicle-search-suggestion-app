package com.vehicle.suggestion.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationSuggestRequest {
    private String brand;
    private String model;
    private String engine;
    private Integer makeYear;
    private Double totalDistance;
}
