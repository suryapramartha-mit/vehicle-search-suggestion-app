package com.vehicle.suggestion.app.util;

public class DistanceConversionUtil {
    private static final Double MILES_TO_KM = 1.60934;

    private DistanceConversionUtil() {}

    public static Double toKm(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Distance value cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Distance value cannot be negative");
        }
        return value * MILES_TO_KM;
    }

    public static Double toMiles(Double value) {
        if (value == null) {
            throw new IllegalArgumentException("Distance value cannot be null");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Distance value cannot be negative");
        }
        return value / MILES_TO_KM;
    }
}
