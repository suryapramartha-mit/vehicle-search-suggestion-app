package com.vehicle.suggestion.app.controller;

import com.vehicle.suggestion.app.dto.CreateOperationRequest;
import com.vehicle.suggestion.app.entity.Operation;
import com.vehicle.suggestion.app.service.OperationService;
import com.vehicle.suggestion.app.util.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operation")
@Validated
public class OperationController {

    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<ApiResponse<Operation>> createOperation(@RequestBody @Valid CreateOperationRequest request) {
        var operation = operationService.createOperation(request);
        return ResponseEntity.ok(ApiResponse.success(operation, "Operation created successfully"));
    }
}
