package com.vehicle.suggestion.app.controller;

import com.vehicle.suggestion.app.dto.*;
import com.vehicle.suggestion.app.entity.Operations;
import com.vehicle.suggestion.app.service.OperationService;
import com.vehicle.suggestion.app.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
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
    @Operation(summary = "Create new Operation")
    @Tag(name = "Operation API")
    public ResponseEntity<ApiResponse<OperationResponse>> createOperation(@RequestBody @Valid CreateOperationRequest request,
                                                                          @RequestParam(required = false, defaultValue = "km") String unit) {
        var newOperation = operationService.createOperation(request, unit);
        var response = new OperationResponse(newOperation, unit);
        return ResponseEntity.ok(ApiResponse.success(response, "Operation created successfully"));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Update existing operation")
    @Tag(name = "Operation API")
    public ResponseEntity<ApiResponse<OperationResponse>> updateOperation(
            @PathVariable Long id,
            @RequestBody UpdateOperationRequest request,
            @RequestParam(required = false, defaultValue = "km") String unit) {
        Operations updatedOperation = operationService.updateOperation(id, request, unit);
        var response = new OperationResponse(updatedOperation, unit);
        return ResponseEntity.ok(ApiResponse.success(response, "Operation updated successfully"));
    }

    @GetMapping(value = "/search", produces = "application/json")
    @Operation(summary = "Search operation with various filters")
    @Tag(name = "Operation API")
    public ResponseEntity<ApiResponse<OperationSearchResponse>> searchOperation(
            OperationSearchRequest request,
            @RequestParam(required = false, defaultValue = "km") String unit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = operationService.searchOperation(request, unit, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(result, "Success"));
    }

}
