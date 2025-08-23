package com.vehicle.suggestion.app.controller;

import com.vehicle.suggestion.app.dto.CreateOperationRequest;
import com.vehicle.suggestion.app.dto.CreateOperationResponse;
import com.vehicle.suggestion.app.service.OperationService;
import com.vehicle.suggestion.app.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/operation")
@Validated
public class OperationController {

    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @PostMapping(produces = "application/json")
    @Operation(summary = "Create new Operation")
    @Tag(name = "Operation API")
    public ResponseEntity<ApiResponse<CreateOperationResponse>> createOperation(@RequestBody @Valid CreateOperationRequest request) {
        var newOperation = operationService.createOperation(request);
        var response = new CreateOperationResponse(newOperation);
        return ResponseEntity.ok(ApiResponse.success(response, "Operation created successfully"));
    }
}
