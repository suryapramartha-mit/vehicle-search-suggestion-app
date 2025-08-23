package com.vehicle.suggestion.app.controller;

import com.vehicle.suggestion.app.dto.CreateOperationRequest;
import com.vehicle.suggestion.app.dto.CreateOperationResponse;
import com.vehicle.suggestion.app.dto.OperationSearchRequest;
import com.vehicle.suggestion.app.dto.UpdateOperationRequest;
import com.vehicle.suggestion.app.entity.Operations;
import com.vehicle.suggestion.app.service.OperationService;
import com.vehicle.suggestion.app.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResponse<CreateOperationResponse>> createOperation(@RequestBody @Valid CreateOperationRequest request) {
        var newOperation = operationService.createOperation(request);
        var response = new CreateOperationResponse(newOperation);
        return ResponseEntity.ok(ApiResponse.success(response, "Operation created successfully"));
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Update existing operation")
    @Tag(name = "Operation API")
    public ResponseEntity<ApiResponse<CreateOperationResponse>> updateOperation(
            @PathVariable Long id,
            @RequestBody UpdateOperationRequest request) {
        Operations updatedOperation = operationService.updateOperation(id, request);
        var response = new CreateOperationResponse(updatedOperation);
        return ResponseEntity.ok(ApiResponse.success(response, "Operation updated successfully"));
    }

    @GetMapping(value = "/search", produces = "application/json")
    @Operation(summary = "Search operation with various filters")
    @Tag(name = "Operation API")
    public ResponseEntity<ApiResponse<Page<Operations>>> searchOperation(
            OperationSearchRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var result = operationService.searchOperation(request, PageRequest.of(page, size));
        return ResponseEntity.ok(ApiResponse.success(result, "Success"));
    }

}
