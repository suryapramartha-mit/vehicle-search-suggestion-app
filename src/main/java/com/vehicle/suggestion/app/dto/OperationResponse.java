package com.vehicle.suggestion.app.dto;

import com.vehicle.suggestion.app.entity.Operations;
import com.vehicle.suggestion.app.enums.DistanceUnit;
import com.vehicle.suggestion.app.util.DistanceConversionUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OperationResponse {
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

    public OperationResponse(Operations newOperation, String unit) {
        this.id = newOperation.getId();
        this.brand = newOperation.getBrand();
        this.model = newOperation.getModel();
        this.engine = newOperation.getEngine();
        this.approxCost = newOperation.getApproxCost();
        this.yearEnd = newOperation.getYearEnd();
        this.yearStart = newOperation.getYearStart();
        this.name = newOperation.getName();
        this.description = newOperation.getDescription();
        this.time = newOperation.getTime();

        if(unit.equalsIgnoreCase(DistanceUnit.MILES.getValue())) {
            this.distanceStart = DistanceConversionUtil.toMiles(newOperation.getDistanceStart());
            this.distanceEnd = DistanceConversionUtil.toMiles(newOperation.getDistanceEnd());
        } else {
            this.distanceEnd = newOperation.getDistanceEnd();
            this.distanceStart = newOperation.getDistanceStart();
        }
    }

    public OperationResponse(OperationSearchResult searchResult, String unit) {
        this.id = searchResult.getId();
        this.brand = searchResult.getBrand();
        this.model = searchResult.getModel();
        this.engine = searchResult.getEngine();
        this.approxCost = searchResult.getApproxCost();
        this.yearEnd = searchResult.getYearEnd();
        this.yearStart = searchResult.getYearStart();
        this.name = searchResult.getName();
        this.description = searchResult.getDescription();
        this.time = searchResult.getTime();

        if(unit.equalsIgnoreCase(DistanceUnit.MILES.getValue())) {
            this.distanceStart = DistanceConversionUtil.toMiles(searchResult.getDistanceStart());
            this.distanceEnd = DistanceConversionUtil.toMiles(searchResult.getDistanceEnd());
        } else {
            this.distanceEnd = searchResult.getDistanceEnd();
            this.distanceStart = searchResult.getDistanceStart();
        }
    }
}
