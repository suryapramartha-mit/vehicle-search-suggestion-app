package com.vehicle.suggestion.app;

import com.vehicle.suggestion.app.dto.CreateOperationRequest;
import com.vehicle.suggestion.app.dto.OperationSearchRequest;
import com.vehicle.suggestion.app.dto.UpdateOperationRequest;
import com.vehicle.suggestion.app.entity.Operations;
import com.vehicle.suggestion.app.exeptions.DataNotFoundException;
import com.vehicle.suggestion.app.repository.OperationRepository;
import com.vehicle.suggestion.app.service.OperationService;
import com.vehicle.suggestion.app.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OperationServiceTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private VehicleService vehicleService;

    @InjectMocks
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //create operation
    @Test
    void createOperation_Success() {
        CreateOperationRequest request = new CreateOperationRequest();
        request.setBrand("Toyota");
        request.setModel("Camry");
        request.setEngine("2.5L");
        request.setYearStart(2015);
        request.setYearEnd(2023);
        request.setDistanceStart(10000.0);
        request.setDistanceEnd(30000.0);
        request.setName("Oil Change");
        request.setApproxCost(90.0);
        request.setDescription("Standard oil change");
        request.setTime(2);
        Operations savedOperation = new Operations();
        savedOperation.setId(1L);
        savedOperation.setBrand("Toyota");
        savedOperation.setModel("Camry");
        when(vehicleService.isVehicleExist("Toyota", "Camry", "2.5L")).thenReturn(true);
        when(operationRepository.save(any(Operations.class))).thenReturn(savedOperation);


        Operations result = operationService.createOperation(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Toyota", result.getBrand());
        verify(vehicleService, times(1)).isVehicleExist("Toyota", "Camry", "2.5L");
        verify(operationRepository, times(1)).save(any(Operations.class));
    }

    @Test
    void createOperation_Error_VehicleNotExist() {
        String brandTest = "Honda";
        String modelTest = "Jazz";
        String engineTest = "1.6L";
        CreateOperationRequest request = new CreateOperationRequest();
        request.setBrand(brandTest);
        request.setModel(modelTest);
        request.setEngine(engineTest);
        when(vehicleService.isVehicleExist(brandTest, modelTest, engineTest)).thenReturn(false);

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> operationService.createOperation(request));

        assertEquals("Vehicle not found", exception.getMessage());
        verify(vehicleService, times(1)).isVehicleExist(brandTest, modelTest, engineTest);
        verify(operationRepository, never()).save(any(Operations.class));
    }

    //Update operation
    @Test
    void updateOperation_Success() {
        Long id = 1L;
        String nameTest = "test name";
        Double approxCostTest = 150.0;
        String descTest = "desc test";

        // Existing Operation
        Operations existingOperation = new Operations();
        existingOperation.setId(id);
        existingOperation.setName("Old Name");
        existingOperation.setApproxCost(100.0);
        existingOperation.setDescription("Old Description");

        when(operationRepository.findById(id)).thenReturn(Optional.of(existingOperation));

        UpdateOperationRequest request = new UpdateOperationRequest();
        request.setName(nameTest);
        request.setApproxCost(approxCostTest);
        request.setDescription(descTest);

        when(operationRepository.save(any(Operations.class))).thenAnswer(i -> i.getArguments()[0]);
        Operations updatedOperation = operationService.updateOperation(id, request);

        assertEquals(nameTest, updatedOperation.getName());
        assertEquals(approxCostTest, updatedOperation.getApproxCost());
        assertEquals(descTest, updatedOperation.getDescription());
    }


    @Test
    void updateOperation_Error_OperationNotFound() {
        Long id = 1L;

        when(operationRepository.findById(id)).thenReturn(Optional.empty());

        UpdateOperationRequest request = new UpdateOperationRequest();
        request.setName("test name");

        assertThrows(DataNotFoundException.class, () -> operationService.updateOperation(id, request));
        verify(operationRepository, never()).save(any());
    }

    //search operation
    @Test
    void searchOperation_Success() {
        OperationSearchRequest request = new OperationSearchRequest();
        request.setBrand("Honda");
        request.setModel("Civic");
        request.setEngine("1.5L Turbo");
        request.setYearStart(2010);
        request.setYearEnd(2020);
        request.setDistanceStart(1000.0);
        request.setDistanceEnd(50000.0);
        PageRequest pageRequest = PageRequest.of(0, 10);

        Operations operation = new Operations();
        Page<Operations> testResult = new PageImpl<>(List.of(operation));

        when(operationRepository.searchOperations(
                request.getBrand(),
                request.getModel(),
                request.getEngine(),
                request.getYearStart(),
                request.getYearEnd(),
                request.getDistanceStart(),
                request.getDistanceEnd(),
                pageRequest
        )).thenReturn(testResult);

        Page<Operations> result = operationService.searchOperation(request, pageRequest);

        // Assert only repo being hit since all query are handled by JPQL
        assertThat(result).isEqualTo(testResult);
        verify(operationRepository, times(1)).searchOperations(
                request.getBrand(),
                request.getModel(),
                request.getEngine(),
                request.getYearStart(),
                request.getYearEnd(),
                request.getDistanceStart(),
                request.getDistanceEnd(),
                pageRequest
        );
    }
}