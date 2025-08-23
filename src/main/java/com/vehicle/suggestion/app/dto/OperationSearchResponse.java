package com.vehicle.suggestion.app.dto;

import com.vehicle.suggestion.app.entity.Operations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Setter
@Builder
@Getter
public class OperationSearchResponse {
    List<OperationDTO> operationsList;
    Pageable pageable;
}
