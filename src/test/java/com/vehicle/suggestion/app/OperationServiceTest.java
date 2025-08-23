package com.vehicle.suggestion.app;

import com.vehicle.suggestion.app.dto.CreateOperationRequest;
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
}