package com.example.sem4_project.exception;

import com.example.sem4_project.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.data.repository.query.QueryCreationException;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getStatus()).body(
                ApiResponse.builder()
                        .code(errorCode.getStatus().value())
                        .message(ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");

        return ResponseEntity.badRequest().body(
                ApiResponse.builder()
                        .code(400)
                        .message(errorMessage)
                        .build()
        );
    }

    @ExceptionHandler(BeanCreationException.class)
    public ResponseEntity<ApiResponse<Object>> handleBeanCreationException(BeanCreationException ex) {
        return ResponseEntity.status(500).body(
                ApiResponse.builder()
                        .code(500)
                        .message("Bean creation failed: " + ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(QueryCreationException.class)
    public ResponseEntity<ApiResponse<Object>> handleQueryCreationException(QueryCreationException ex) {
        return ResponseEntity.status(500).body(
                ApiResponse.builder()
                        .code(500)
                        .message("Query creation failed: " + ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleOtherExceptions(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(
                ApiResponse.builder()
                        .code(500)
                        .message("Something went wrong: " + ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKeyException(DuplicateKeyException ex) {
        // Gửi thông báo lỗi tùy chỉnh về Duplicate Key
        return new ResponseEntity<>("Duplicate entry detected for email or other unique fields: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
            return new ResponseEntity<>("Duplicate entry detected: " + ex.getCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Data integrity violation: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.VALIDATION_FAILED, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.VALIDATION_FAILED, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.INVALID_INPUT, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        return new ResponseEntity<>(ApiResponse.error(ErrorCode.OPERATION_FAILED, "An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
