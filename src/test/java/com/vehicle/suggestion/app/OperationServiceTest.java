package com.vehicle.suggestion.app;

import com.vehicle.suggestion.app.dto.*;
import com.vehicle.suggestion.app.entity.Operations;
import com.vehicle.suggestion.app.enums.DistanceUnit;
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


        Operations result = operationService.createOperation(request, DistanceUnit.KM.getValue());

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

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> operationService.createOperation(request, DistanceUnit.KM.getValue()));

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
        Operations updatedOperation = operationService.updateOperation(id, request, DistanceUnit.KM.getValue());

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

        assertThrows(DataNotFoundException.class, () -> operationService.updateOperation(id, request, DistanceUnit.KM.getValue()));
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

        OperationSearchResult operation = new OperationSearchResult();
        operation.setBrand("Honda");
        operation.setModel("Civic");
        Page<OperationSearchResult> testResult = new PageImpl<>(List.of(operation), pageRequest, 1);

        when(operationRepository.searchOperations(
                null,
                null,
                request.getEngine(),
                request.getYearStart(),
                request.getYearEnd(),
                request.getDistanceStart(),
                request.getDistanceEnd(),
                pageRequest
        )).thenReturn(testResult);

        OperationSearchResponse response = operationService.searchOperation(request, DistanceUnit.KM.getValue(), pageRequest);

        assertNotNull(response);
        assertEquals(1, response.getOperationsList().size());
        verify(operationRepository, times(1)).searchOperations(
                null,
                null,
                request.getEngine(),
                request.getYearStart(),
                request.getYearEnd(),
                request.getDistanceStart(),
                request.getDistanceEnd(),
                pageRequest
        );
    }

    //suggest operation
    @Test
    void suggestOperation_Success_ReturnBetweenRanges() {
        prepareSuggestionData();

        var request = new OperationSuggestRequest();
        request.setBrand("Ford");
        request.setModel("Bronco");
        request.setEngine("2.7L EcoBoost");
        request.setMakeYear(2022);
        request.setTotalDistance(65000.0); // Between dataTest1 and dataTest2
        String unit = "km";

        List<OperationResponse> result = operationService.suggestOperations(request, unit);

        assertEquals(2, result.size());
    }

    @Test
    void suggestOperation_Success_inRanges() {
        prepareSuggestionData();

        var request = new OperationSuggestRequest();
        request.setBrand("Ford");
        request.setModel("Bronco");
        request.setEngine("2.7L EcoBoost");
        request.setMakeYear(2022);
        request.setTotalDistance(30000.0); // in range of dataTest1
        String unit = "km";

        List<OperationResponse> result = operationService.suggestOperations(request, unit);

        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getId().equals(1L)));
    }

    @Test
    void suggestOperation_Success_HighInRange() {
        prepareSuggestionData();

        var request = new OperationSuggestRequest();
        request.setBrand("Ford");
        request.setModel("Bronco");
        request.setEngine("2.7L EcoBoost");
        request.setMakeYear(2022);
        request.setTotalDistance(300000.0); // above range of dataTest
        String unit = "km";

        List<OperationResponse> result = operationService.suggestOperations(request, unit);

        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getId().equals(2L)));
    }

    @Test
    void suggestOperation_Success_LowInRange() {
        prepareSuggestionData();

        var request = new OperationSuggestRequest();
        request.setBrand("Ford");
        request.setModel("Bronco");
        request.setEngine("2.7L EcoBoost");
        request.setMakeYear(2022);
        request.setTotalDistance(10000.0); // below range of dataTest
        String unit = "km";

        List<OperationResponse> result = operationService.suggestOperations(request, unit);

        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getId().equals(1L)));
    }

    @Test
    void suggestOperation_Success_ReturnAll_IfNoDistance() {
        prepareSuggestionData();

        var request = new OperationSuggestRequest();
        request.setBrand("Ford");
        request.setModel("Bronco");
        request.setEngine("2.7L EcoBoost");
        request.setMakeYear(2022);
        String unit = "km";

        List<OperationResponse> result = operationService.suggestOperations(request, unit);

        assertEquals(2, result.size());
    }

    private void prepareSuggestionData() {
        OperationSearchResult dataTest1 = new OperationSearchResult();
        dataTest1.setId(1L);
        dataTest1.setDistanceStart(30000.0);
        dataTest1.setDistanceEnd(60000.0);
        dataTest1.setApproxCost(20.0);

        OperationSearchResult dataTest2 = new OperationSearchResult();
        dataTest2.setId(2L);
        dataTest2.setDistanceStart(70000.0);
        dataTest2.setDistanceEnd(120000.0);
        dataTest2.setApproxCost(30.0);

        List<OperationSearchResult> queryResult = List.of(dataTest1, dataTest2);

        when(operationRepository.suggestOperations("Ford", "Bronco", "2.7L EcoBoost", 2022))
                .thenReturn(queryResult);
    }
}