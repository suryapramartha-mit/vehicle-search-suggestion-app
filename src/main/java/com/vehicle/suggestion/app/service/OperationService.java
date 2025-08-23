package com.vehicle.suggestion.app.service;

import com.vehicle.suggestion.app.dto.CreateOperationRequest;
import com.vehicle.suggestion.app.dto.OperationSearchRequest;
import com.vehicle.suggestion.app.dto.UpdateOperationRequest;
import com.vehicle.suggestion.app.entity.Operations;
import com.vehicle.suggestion.app.exeptions.DataNotFoundException;
import com.vehicle.suggestion.app.repository.OperationRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Operations updateOperation(Long id, UpdateOperationRequest request) {
        Operations existingOperation = operationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Operation not found with id: " + id));

        // Update only fields that are provided
        Optional.ofNullable(request.getName()).ifPresent(existingOperation::setName);
        Optional.ofNullable(request.getApproxCost()).ifPresent(existingOperation::setApproxCost);
        Optional.ofNullable(request.getDescription()).ifPresent(existingOperation::setDescription);
        Optional.ofNullable(request.getTime()).ifPresent(existingOperation::setTime);
        Optional.ofNullable(request.getDistanceStart()).ifPresent(existingOperation::setDistanceStart);
        Optional.ofNullable(request.getDistanceEnd()).ifPresent(existingOperation::setDistanceEnd);
        Optional.ofNullable(request.getYearStart()).ifPresent(existingOperation::setYearStart);
        Optional.ofNullable(request.getYearEnd()).ifPresent(existingOperation::setYearEnd);

        return operationRepository.save(existingOperation);
    }

    public Page<Operations> searchOperation(OperationSearchRequest request, PageRequest pageRequest) {
        return operationRepository.searchOperations(
                request.getBrand(),
                request.getModel(),
                request.getEngine(),
                request.getYearStart(),
                request.getYearEnd(),
                request.getDistanceStart(),
                request.getDistanceEnd(),
                pageRequest);
    }
}
