package com.example.sem4_project.exception;

import com.example.sem4_project.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.data.repository.query.QueryCreationException;

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
}
