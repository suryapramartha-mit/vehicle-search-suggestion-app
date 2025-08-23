package com.vehicle.suggestion.app.service;

import com.vehicle.suggestion.app.dto.CreateOperationRequest;
import com.vehicle.suggestion.app.entity.Operations;
import com.vehicle.suggestion.app.exeptions.DataNotFoundException;
import com.vehicle.suggestion.app.repository.OperationRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class OperationService {
    private OperationRepository operationRepository;
    private VehicleService vehicleService;

    public OperationService(OperationRepository operationRepository, VehicleService vehicleService) {
        this.operationRepository = operationRepository;
        this.vehicleService = vehicleService;
    }

    public Operations createOperation(@Valid CreateOperationRequest request) {
        var isVehicleExist = vehicleService.isVehicleExist(request.getBrand(), request.getModel(), request.getEngine());
        if (!isVehicleExist) {
            throw new DataNotFoundException("Vehicle not found");
        }
        return operationRepository.save(Operations.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .engine(request.getEngine())
                .yearEnd(request.getYearEnd())
                .yearStart(request.getYearStart())
                .distanceEnd(request.getDistanceEnd())
                .distanceStart(request.getDistanceStart())
                .approxCost(request.getApproxCost())
                .description(request.getDescription())
                .time(request.getTime())
                .name(request.getName())
                .build());
    }
}
