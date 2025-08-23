package com.vehicle.suggestion.app.exeptions;

import com.vehicle.suggestion.app.dto.ErrorDTO;
import com.vehicle.suggestion.app.util.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorDTO> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new ErrorDTO(error.getField(), error.getDefaultMessage()))
                .toList();

        ApiResponse<Object> response = ApiResponse.error(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataNotFoundException(DataNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(HttpStatus.NOT_FOUND.value(),
                        "Data not found",
                        List.of(new ErrorDTO("dataNotFound", ex.getMessage()))
                ));
    }


    // for all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        log.error("Unexpected error occurred", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "An unexpected error occurred",
                        List.of(new ErrorDTO("exception", ex.getMessage()))
                ));
    }
}
