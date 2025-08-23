package com.vehicle.suggestion.app.service;

import com.vehicle.suggestion.app.repository.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public boolean isVehicleExist(String brand, String model, String engine) {
        return vehicleRepository.findByBrandAndModelAndEngine(brand, model, engine).isPresent();
    }
}
