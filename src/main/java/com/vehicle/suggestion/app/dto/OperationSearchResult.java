package com.vehicle.suggestion.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OperationSearchResult {
    private Long id;
    private String brand;
    private String model;
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

