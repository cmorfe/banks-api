package com.cmorfe.banks.api.infrastructure.configuration;

import com.cmorfe.banks.api.infrastructure.interfaces.dto.ErrorResponse;
import com.cmorfe.banks.api.infrastructure.interfaces.dto.ValidationErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Objects;

import static com.cmorfe.banks.api.infrastructure.configuration.GlobalExceptionHandler.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final String FIELD_NAME = "field";
    private static final String INVALID = "invalid";
    private static final String INVALID_FIELD_VALUE = "Invalid value for field field: invalid";
    private static final String OBJECT_NAME = "objectName";
    private static final String DEFAULT_ERROR_MESSAGE = "error";
    private static final String INVALID_ARGUMENT = "Invalid argument";
    private static final String FIELD_VALUE_MUST_BE_ONE_OF = "Field field must be one of [VALUE1, VALUE2]";
    private static final String VALUE_IS_EXPECTED_TO_BE_OF_TYPE = "'field' is expected to be of type 'String'";
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

    @Mock
    private InvalidFormatException invalidFormatException;

    @Mock
    private EntityNotFoundException entityNotFoundException;

    @Mock
    private NoResourceFoundException noResourceFoundException;

    @Mock
    private DataIntegrityViolationException dataIntegrityViolationException;

    private void assertErrorResponse(ResponseEntity<ErrorResponse> response, HttpStatus status, String message, String details) {
        assertEquals(status, response.getStatusCode());
        assertEquals(message, Objects.requireNonNull(response.getBody()).getMessage());
        assertEquals(details, response.getBody().getDetails());
    }

    private void mockFieldError() {
        FieldError fieldError = new FieldError(OBJECT_NAME, GlobalExceptionHandlerTest.FIELD_NAME, DEFAULT_ERROR_MESSAGE);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
    }

    private void mockMethodArgumentTypeMismatchException() {
        when(methodArgumentTypeMismatchException.getName()).thenReturn(GlobalExceptionHandlerTest.FIELD_NAME);
        when(methodArgumentTypeMismatchException.getRequiredType()).thenReturn((Class) String.class);
    }

    private void mockInvalidFormatException(Class<?> targetType) {
        when(invalidFormatException.getPath()).thenReturn(List.of(new JsonMappingException.Reference(null, FIELD_NAME)));
        when(invalidFormatException.getValue()).thenReturn(INVALID);
        when(invalidFormatException.getTargetType()).thenReturn((Class) targetType);
    }

    private enum TestEnum {
        VALUE1, VALUE2
    }

    @Nested
    class HandleValidationExceptionsTests {
        @Test
        void shouldHandleValidationExceptions() {
            mockFieldError();

            ResponseEntity<ValidationErrorResponse> response = globalExceptionHandler
                    .handleValidationExceptions(methodArgumentNotValidException);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(1, Objects.requireNonNull(response.getBody()).getErrors().size());
        }
    }

    @Nested
    class HandleMethodArgumentTypeMismatchExceptionTests {
        @Test
        void shouldHandleMethodArgumentTypeMismatchException() {
            mockMethodArgumentTypeMismatchException();

            ResponseEntity<ErrorResponse> response = globalExceptionHandler
                    .handleMethodArgumentTypeMismatchException(methodArgumentTypeMismatchException);

            assertErrorResponse(
                    response,
                    HttpStatus.BAD_REQUEST,
                    INVALID_FIELD_TYPE,
                    VALUE_IS_EXPECTED_TO_BE_OF_TYPE
            );
        }
    }

    @Nested
    class HandleEntityNotFoundExceptionTests {
        @Test
        void shouldHandleEntityNotFoundException() {
            when(entityNotFoundException.getMessage()).thenReturn(NOT_FOUND);

            ResponseEntity<ErrorResponse> response = globalExceptionHandler
                    .handleEntityNotFoundException(entityNotFoundException);

            assertErrorResponse(response, HttpStatus.NOT_FOUND, NOT_FOUND, NOT_FOUND);
        }
    }

    @Nested
    class HandleNoResourceFoundExceptionTests {
        @Test
        void shouldHandleNoResourceFoundException() {
            when(noResourceFoundException.getMessage()).thenReturn(NOT_FOUND);

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNoResourceFoundException(noResourceFoundException);

            assertErrorResponse(response, HttpStatus.NOT_FOUND, NOT_FOUND, NOT_FOUND);
        }
    }

    @Nested
    class HandleDataIntegrityViolationExceptionTests {
        @Test
        void shouldHandleDataIntegrityViolationExceptionForBankName() {
            when(dataIntegrityViolationException.getMessage()).thenReturn(BANKS_NAME_UNIQUE_INDEX);

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDataIntegrityViolationException(dataIntegrityViolationException);

            assertErrorResponse(response, HttpStatus.CONFLICT, DATA_INTEGRITY_VIOLATION, BANK_NAME_ALREADY_EXISTS);
        }

        @Test
        void shouldHandleDataIntegrityViolationExceptionForBranchCode() {
            when(dataIntegrityViolationException.getMessage()).thenReturn(BRANCHES_CODE_UNIQUE_INDEX);

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDataIntegrityViolationException(dataIntegrityViolationException);

            assertErrorResponse(response, HttpStatus.CONFLICT, DATA_INTEGRITY_VIOLATION, BRANCH_CODE_ALREADY_EXISTS);
        }
    }

    @Nested
    class HandleIllegalArgumentExceptionTests {
        @Test
        void shouldHandleIllegalArgumentException() {
            IllegalArgumentException exception = new IllegalArgumentException(INVALID_ARGUMENT);

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

            assertErrorResponse(response, HttpStatus.BAD_REQUEST, INVALID_FORMAT_ERROR, INVALID_ARGUMENT);
        }
    }

    @SuppressWarnings("deprecation")
    @Nested
    class HandleHttpMessageNotReadableExceptionTests {
        @Nested
        class HandleInvalidFormatExceptionTests {
            @Test
            void shouldHandleInvalidFormatException() {
                mockInvalidFormatException(String.class);

                ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidFormatException(invalidFormatException);

                assertErrorResponse(response, HttpStatus.BAD_REQUEST, INVALID_FIELD_VALUE, INVALID_VALUE);
            }

            @Test
            void shouldHandleInvalidFormatExceptionForEnum() {
                mockInvalidFormatException(TestEnum.class);

                ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleInvalidFormatException(invalidFormatException);

                assertErrorResponse(response, HttpStatus.BAD_REQUEST, INVALID_FIELD_VALUE, FIELD_VALUE_MUST_BE_ONE_OF);
            }
        }

        @Test
        void shouldHandleHttpMessageNotReadableException() {
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException(REQUIRED_REQUEST_BODY);

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpMessageNotReadableException(exception);

            assertErrorResponse(response, HttpStatus.BAD_REQUEST, BAD_REQUEST, REQUIRED_REQUEST_BODY);
        }

        @Test
        void shouldHandleMismatchedInputException() {
            MismatchedInputException exception = new MismatchedInputException(null, "Mismatched input", String.class) {
            };

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpMessageNotReadableException(new HttpMessageNotReadableException(exception.getMessage(), exception));

            assertErrorResponse(response, HttpStatus.BAD_REQUEST, INVALID_FORMAT_ERROR, INVALID_FORMAT_TYPE_BRANCH);
        }

        @Test
        void shouldHandleJsonParseException() {
            JsonParseException exception = new JsonParseException(null, JSON_PARSE_ERROR);

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpMessageNotReadableException(new HttpMessageNotReadableException(exception.getMessage(), exception));

            assertErrorResponse(response, HttpStatus.BAD_REQUEST, JSON_PARSE_ERROR, JSON_PARSE_ERROR);
        }

        @Test
        void shouldHandleDefaultHttpMessageNotReadableException() {
            HttpMessageNotReadableException exception = new HttpMessageNotReadableException(INVALID_REQUEST_BODY);

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpMessageNotReadableException(exception);

            assertErrorResponse(response, HttpStatus.BAD_REQUEST, BAD_REQUEST, REQUIRED_REQUEST_BODY);
        }
    }

    @Nested
    class HandleGeneralExceptionsTests {
        @Test
        void shouldHandleGeneralExceptions() {
            Exception exception = new Exception(INTERNAL_SERVER_ERROR);

            ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGeneralExceptions(exception);

            assertErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
        }
    }
}