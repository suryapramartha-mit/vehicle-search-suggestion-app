package com.vehicle.suggestion.app.dto;

import com.vehicle.suggestion.app.entity.Operations;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOperationResponse {
    private final Long id;
    private final String brand;
    private final String model;
    private final String engine;
    private final Integer yearStart;
    private final Integer yearEnd;
    private final Double distanceStart;
    private final Double distanceEnd;
    private final String name;
    private final Double approxCost;
    private final String description;
    private final Integer time;

    public CreateOperationResponse(Operations newOperation) {
        this.id = newOperation.getId();
        this.brand = newOperation.getBrand();
        this.model = newOperation.getModel();
        this.engine = newOperation.getEngine();
        this.approxCost = newOperation.getApproxCost();
        this.distanceEnd = newOperation.getDistanceEnd();
        this.distanceStart = newOperation.getDistanceStart();
        this.yearEnd = newOperation.getYearEnd();
        this.yearStart = newOperation.getYearStart();
        this.name = newOperation.getName();
        this.description = newOperation.getDescription();
        this.time = newOperation.getTime();
    }
}
