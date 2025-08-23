package com.vehicle.suggestion.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOperationRequest {
    private String name;
    private Double approxCost;
    private String description;
    private Integer time;
    private Double distanceStart;
    private Double distanceEnd;
    private Integer yearStart;
    private Integer yearEnd;
}
