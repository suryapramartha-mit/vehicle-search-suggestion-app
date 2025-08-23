package com.vehicle.suggestion.app.service;

import com.vehicle.suggestion.app.dto.*;
import com.vehicle.suggestion.app.entity.Operations;
import com.vehicle.suggestion.app.enums.DistanceUnit;
import com.vehicle.suggestion.app.exeptions.DataNotFoundException;
import com.vehicle.suggestion.app.repository.OperationRepository;
import com.vehicle.suggestion.app.util.DistanceConversionUtil;
import com.vehicle.suggestion.app.util.TrigramUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OperationService {
    private final OperationRepository operationRepository;
    private final VehicleService vehicleService;

    public OperationService(OperationRepository operationRepository, VehicleService vehicleService) {
        this.operationRepository = operationRepository;
        this.vehicleService = vehicleService;
    }

    public Operations createOperation(@Valid CreateOperationRequest request, String unit) {
        var isVehicleExist = vehicleService.isVehicleExist(request.getBrand(), request.getModel(), request.getEngine());
        if (!isVehicleExist) {
            throw new DataNotFoundException("Vehicle not found");
        }

        Double convertedDistanceStart = convertDistanceIfMilesToKm(request.getDistanceStart(), unit);
        Double convertedDistanceEnd = convertDistanceIfMilesToKm(request.getDistanceEnd(), unit);

        return operationRepository.save(Operations.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .engine(request.getEngine())
                .yearEnd(request.getYearEnd())
                .yearStart(request.getYearStart())
                .distanceEnd(convertedDistanceEnd)
                .distanceStart(convertedDistanceStart)
                .approxCost(request.getApproxCost())
                .description(request.getDescription())
                .time(request.getTime())
                .name(request.getName())
                .build());
    }


    public Operations updateOperation(Long id, UpdateOperationRequest request, String unit) {
        Operations existingOperation = operationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Operation not found with id: " + id));

        Double convertedDistanceStart = convertDistanceIfMilesToKm(request.getDistanceStart(), unit);
        Double convertedDistanceEnd = convertDistanceIfMilesToKm(request.getDistanceEnd(), unit);

        Optional.ofNullable(request.getName()).ifPresent(existingOperation::setName);
        Optional.ofNullable(request.getApproxCost()).ifPresent(existingOperation::setApproxCost);
        Optional.ofNullable(request.getDescription()).ifPresent(existingOperation::setDescription);
        Optional.ofNullable(request.getTime()).ifPresent(existingOperation::setTime);
        Optional.ofNullable(convertedDistanceStart).ifPresent(existingOperation::setDistanceStart);
        Optional.ofNullable(convertedDistanceEnd).ifPresent(existingOperation::setDistanceEnd);
        Optional.ofNullable(request.getYearStart()).ifPresent(existingOperation::setYearStart);
        Optional.ofNullable(request.getYearEnd()).ifPresent(existingOperation::setYearEnd);

        return operationRepository.save(existingOperation);
    }

    public OperationSearchResponse searchOperation(OperationSearchRequest request, String unit, PageRequest pageRequest) {
        Double convertedDistanceStart = convertDistanceIfMilesToMiles(request.getDistanceStart(), unit);
        Double convertedDistanceEnd = convertDistanceIfMilesToMiles(request.getDistanceEnd(), unit);

        var queryResult = operationRepository.searchOperations(
                null,
                null,
                request.getEngine(),
                request.getYearStart(),
                request.getYearEnd(),
                convertedDistanceStart,
                convertedDistanceEnd,
                pageRequest);

        var convertedData = queryResult.getContent()
                .stream()
                .filter(p -> TrigramUtils.trigramMatch(p.getBrand(), request.getBrand())
                        && TrigramUtils.trigramMatch(p.getModel(), request.getModel()))
                .map(res -> new OperationResponse(res, unit))
                .toList();
        return OperationSearchResponse.builder()
                .operationsList(convertedData)
                .pageable(queryResult.getPageable()).build();
    }

    public List<OperationResponse> suggestOperations(OperationSuggestRequest request, String unit) {
        Double convertedTotalDistance = convertDistanceIfMilesToMiles(request.getTotalDistance(), unit);

        var queryResult = operationRepository.suggestOperations(
                request.getBrand(),
                request.getModel(),
                request.getEngine(),
                request.getMakeYear());

        List<OperationSearchResult> suggestedList;
        if (convertedTotalDistance == null) {
            suggestedList = queryResult;
        } else {
            // find between ranges
            suggestedList = queryResult.stream()
                    .filter(op -> convertedTotalDistance >= op.getDistanceStart() && convertedTotalDistance <= op.getDistanceEnd())
                    .collect(Collectors.toList());

            if (suggestedList.isEmpty() && !queryResult.isEmpty()) {
                // find the closest lower and higher ranges
                OperationSearchResult lower = queryResult.stream()
                        .filter(op -> op.getDistanceEnd() < convertedTotalDistance)
                        .max(Comparator.comparing(OperationSearchResult::getDistanceEnd))
                        .orElse(null);

                OperationSearchResult higher = queryResult.stream()
                        .filter(op -> op.getDistanceStart() > convertedTotalDistance)
                        .min(Comparator.comparing(OperationSearchResult::getDistanceStart))
                        .orElse(null);

                suggestedList = new ArrayList<>();
                if (lower != null) suggestedList.add(lower);
                if (higher != null) suggestedList.add(higher);

                // if distance is outside all ranges (too low or too high), pick the closest one
                if (suggestedList.isEmpty()) {
                    OperationSearchResult closest = queryResult.stream()
                            .min(Comparator.comparing(op -> Math.min(
                                    Math.abs(convertedTotalDistance - op.getDistanceStart()),
                                    Math.abs(convertedTotalDistance - op.getDistanceEnd())
                            )))
                            .get();
                    suggestedList.add(closest);
                }
            }
        }

        return suggestedList.stream()
                .map(op -> new OperationResponse(op, unit))
                .sorted(Comparator.comparing(OperationResponse::getApproxCost))
                .toList();
    }

    private Double convertDistanceIfMilesToMiles(Double distance, String unit) {
        if (distance == null) {
            return null;
        }
        return DistanceUnit.MILES.getValue().equalsIgnoreCase(unit) ? DistanceConversionUtil.toMiles(distance) : distance;
    }
    private Double convertDistanceIfMilesToKm(Double distance, String unit) {
        if (distance == null) {
            return null;
        }
        return DistanceUnit.MILES.getValue().equalsIgnoreCase(unit) ? DistanceConversionUtil.toKm(distance) : distance;
    }


}
