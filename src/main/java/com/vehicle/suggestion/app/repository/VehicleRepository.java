package com.vehicle.suggestion.app.repository;

import com.vehicle.suggestion.app.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByBrandAndModelAndEngine(String brand, String model, String engine);
}
