package com.vehicle.suggestion.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateOperationRequest {

    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotBlank
    private String engine;

    private Integer yearStart;
    private Integer yearEnd;
    private Double distanceStart;
    private Double distanceEnd;
    private String name;
    private Double approxCost;
    private String description;
    private Integer time;
}

