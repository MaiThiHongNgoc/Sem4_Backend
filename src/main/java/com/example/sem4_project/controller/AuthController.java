package com.example.sem4_project.controller;

import com.example.sem4_project.dto.request.LoginRequest;
import com.example.sem4_project.dto.response.ApiResponse;
import com.example.sem4_project.dto.response.LoginResponse;
import com.example.sem4_project.exception.ErrorCode;
import com.example.sem4_project.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request, BindingResult bindingResult) {
        // Kiểm tra nếu có lỗi validation
        if (bindingResult.hasErrors()) {
            // Tạo thông báo lỗi từ các lỗi validation
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ApiResponse.<LoginResponse>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(errorMessage)
                    .result(null)
                    .build();
        }

        logger.debug("Received login request for username: {}", request.getUsername());
        try {
            LoginResponse loginResponse = authService.login(request);
            logger.debug("Login successful for username: {}", request.getUsername());
            return ApiResponse.<LoginResponse>builder()
                    .code(HttpStatus.OK.value())
                    .message(ErrorCode.SUCCESS.getMessage())
                    .result(loginResponse)
                    .build();
        } catch (RuntimeException e) {
            logger.error("Login failed for username: {}. Error: {}", request.getUsername(), e.getMessage());
            return ApiResponse.<LoginResponse>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .message(e.getMessage())
                    .result(null)
                    .build();
        }
    }
}
