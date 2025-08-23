package com.vehicle.suggestion.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Setter
@Builder
@Getter
public class OperationSearchResponse {
    List<OperationResponse> operationsList;
    Pageable pageable;
}
