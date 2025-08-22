package com.vehicle.suggestion.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ErrorDTO {
    private String field;
    private String message;
}
