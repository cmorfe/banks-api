package com.cmorfe.banks.api.infrastructure.configuration;

import com.cmorfe.banks.api.infrastructure.error.ErrorResponse;
import com.cmorfe.banks.api.infrastructure.error.ValidationErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    static final String INVALID_FORMAT_ERROR = "Invalid format error";
    static final String NOT_FOUND = "Not found";
    static final String DATA_INTEGRITY_VIOLATION = "Data integrity violation";
    static final String BAD_REQUEST = "Bad Request";
    static final String INTERNAL_SERVER_ERROR = "Internal server error";
    static final String INVALID_FIELD_TYPE = "Invalid field type";
    static final String JSON_PARSE_ERROR = "JSON parse error";
    static final String BRANCHES_CODE_UNIQUE_INDEX = "CONSTRAINT_INDEX_2";
    static final String BANKS_NAME_UNIQUE_INDEX = "CONSTRAINT_INDEX_3";
    static final String BANK_NAME_ALREADY_EXISTS = "A bank with that name already exists";
    static final String BRANCH_CODE_ALREADY_EXISTS = "A branch with that code already exists";
    static final String REQUIRED_REQUEST_BODY = "Required request body is missing";
    static final String INVALID_FORMAT_TYPE_BRANCH = "Invalid format for type Branch";
    static final String INVALID_REQUEST_BODY = "Invalid request body";
    static final String INVALID_VALUE = "Invalid value";
    private static final String VALIDATION_ERROR = "Validation error";
    private static final String INVALID_VALUE_FOR_FIELD = "Invalid value for field %s: %s";
    private static final String FIELD_MUST_BE_ONE_OF = "Field %s must be one of [%s]";
    private static final String IS_EXPECTED_TO_BE_OF_TYPE = "'%s' is expected to be of type '%s'";

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static void logError(String error, Exception exception) {
        logError(error, exception, null);
    }

    private static void logError(String error, Exception exception, String message) {
        if (message == null) {
            message = exception.getMessage();
        }

        logger.error("{}: {}", error, message, exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        List<ValidationErrorResponse.FieldError> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(VALIDATION_ERROR, errors);

        logError(VALIDATION_ERROR, exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        String error = String.format(
                IS_EXPECTED_TO_BE_OF_TYPE,
                exception.getName(),
                Objects.requireNonNull(exception.getRequiredType()).getSimpleName()
        );

        ErrorResponse errorResponse = new ErrorResponse(INVALID_FIELD_TYPE, error);

        logError(INVALID_FIELD_TYPE, exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        Throwable cause = exception.getMostSpecificCause();
        String message = BAD_REQUEST;
        String details;

        switch (cause) {
            case InvalidFormatException invalidFormatException -> {
                return handleInvalidFormatException(invalidFormatException);
            }
            case HttpMessageNotReadableException ignored -> details = REQUIRED_REQUEST_BODY;
            case MismatchedInputException ignored -> {
                message = INVALID_FORMAT_ERROR;

                details = INVALID_FORMAT_TYPE_BRANCH;
            }
            case JsonParseException jsonParseException -> {
                message = JSON_PARSE_ERROR;

                details = jsonParseException.getOriginalMessage();
            }
            default -> details = INVALID_REQUEST_BODY;
        }

        ErrorResponse errorResponse = new ErrorResponse(message, details);

        logError(message, exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    ResponseEntity<ErrorResponse> handleInvalidFormatException(InvalidFormatException exception) {
        String fieldName = exception.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));

        String invalidValue = exception.getValue().toString();

        Class<?> targetType = exception.getTargetType();

        String message = String.format(INVALID_VALUE_FOR_FIELD, fieldName, invalidValue);

        String details = INVALID_VALUE;

        if (targetType.isEnum()) {
            String acceptedValues = Arrays.stream(targetType.getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            details = String.format(FIELD_MUST_BE_ONE_OF, fieldName, acceptedValues);
        }

        ErrorResponse errorResponse = new ErrorResponse(message, details);

        logError(message, exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ErrorResponse errorResponse = new ErrorResponse(INVALID_FORMAT_ERROR, exception.getMessage());

        logError(INVALID_FORMAT_ERROR, exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND, exception.getMessage());

        logError(NOT_FOUND, exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        ErrorResponse errorResponse = new ErrorResponse(NOT_FOUND, exception.getMessage());

        logError(NOT_FOUND, exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String message = exception.getMessage();

        if (message.contains(BANKS_NAME_UNIQUE_INDEX)) {
            message = BANK_NAME_ALREADY_EXISTS;
        }

        if (message.contains(BRANCHES_CODE_UNIQUE_INDEX)) {
            message = BRANCH_CODE_ALREADY_EXISTS;
        }

        ErrorResponse errorResponse = new ErrorResponse(DATA_INTEGRITY_VIOLATION, message);

        logError(DATA_INTEGRITY_VIOLATION, exception, message);

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralExceptions(Exception exception) {
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR, exception.getMessage());

        logError(INTERNAL_SERVER_ERROR, exception);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
