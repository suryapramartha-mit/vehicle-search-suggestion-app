package com.vehicle.suggestion.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RestResponseDTO {
    private Integer code;
    private String message;
}
