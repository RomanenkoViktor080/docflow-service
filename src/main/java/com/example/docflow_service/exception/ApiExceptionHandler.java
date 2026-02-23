package com.example.docflow_service.exception;

import com.example.docflow_service.dto.error.ErrorResponse;
import com.example.docflow_service.dto.error.ValidationErrorDetail;
import com.example.docflow_service.dto.error.ValidationErrorResponse;
import com.example.docflow_service.exception.api.ApiException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    public static final String UNKNOWN_FIELD = "unknown field";

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleJsonParseError(HttpMessageNotReadableException ex) {
        Throwable rootCause = ex.getCause();

        if (rootCause instanceof InvalidFormatException invalid) {
            String field = extractField(invalid.getPath());
            String message = "Invalid format for field '" + field + "'";
            log.error(message, ex);
            return new ErrorResponse(message);
        }
        log.error("Malformed JSON request: {}", ex.getMessage(), ex);
        return new ErrorResponse("Malformed JSON request");
    }

    private static String extractField(List<JsonMappingException.Reference> path) {
        return (path == null || path.isEmpty())
                ? UNKNOWN_FIELD
                : path.getLast().getFieldName();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ValidationErrorResponse handleConstraintViolation(ConstraintViolationException ex) {
        log.error("Constraint violation", ex);
        List<ValidationErrorDetail> details = ex.getConstraintViolations().stream()
                .map(this::mapToValidationErrorDetail)
                .toList();
        return new ValidationErrorResponse(
                "Constraint violation",
                "Validation failed for one or more fields.",
                details
        );
    }

    private ValidationErrorDetail mapToValidationErrorDetail(ConstraintViolation<?> violation) {
        String fieldName = extractFieldName(violation.getPropertyPath());
        return new ValidationErrorDetail(
                fieldName,
                violation.getMessage(),
                violation.getInvalidValue()
        );
    }

    private String extractFieldName(Path propertyPath) {
        String fieldName = null;
        for (Path.Node node : propertyPath) {
            fieldName = node.getName();
        }
        return fieldName;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
        log.error("message={}", ex.getDebugMessage(), ex);
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleApiException(Exception ex) {
        log.error("Unhandled exception, message={}", ex.getMessage(), ex);
        return new ErrorResponse("Internal server error");
    }
}

