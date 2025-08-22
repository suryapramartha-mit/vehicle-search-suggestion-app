package com.vehicle.suggestion.app.service;

import com.vehicle.suggestion.app.dto.CreateOperationRequest;
import com.vehicle.suggestion.app.entity.Operation;
import com.vehicle.suggestion.app.repository.OperationRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class OperationService {

    private OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    public Operation createOperation(@Valid CreateOperationRequest operation) {
        var data = new Operation();
        return operationRepository.save(data);
    }
}
