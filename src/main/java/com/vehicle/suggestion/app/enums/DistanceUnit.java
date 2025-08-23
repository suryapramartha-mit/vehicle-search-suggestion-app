package com.vehicle.suggestion.app.enums;

public enum DistanceUnit {
    KM("km"),
    MILES("miles");

    final String value;

    public String getValue(){
        return value;
    }
    DistanceUnit(String value) {
        this.value = value;
    }
}
