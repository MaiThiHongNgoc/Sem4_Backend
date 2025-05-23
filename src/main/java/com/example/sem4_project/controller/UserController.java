package com.example.sem4_project.controller;

import com.example.sem4_project.dto.request.UserRequest;
import com.example.sem4_project.dto.response.ApiResponse;
import com.example.sem4_project.dto.response.UserResponse;
import com.example.sem4_project.exception.ErrorCode;
import com.example.sem4_project.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsers(
            @RequestParam(required = false) String status
    ) {
        logger.info("Received getUsers request: status={}", status);
        List<UserResponse> users = userService.getUsers(status);
        return new ResponseEntity<>(ApiResponse.success(ErrorCode.SUCCESS, users), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @RequestBody @Valid UserRequest request,
            BindingResult bindingResult
    ) {
        logger.info("Received register request for username={}", request.getUsername());
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            logger.warn("Validation failed: {}", errorMessage);
            return new ResponseEntity<>(ApiResponse.error(ErrorCode.VALIDATION_FAILED, errorMessage), HttpStatus.BAD_REQUEST);
        }
        ApiResponse<UserResponse> response = userService.register(request, bindingResult);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID userId,
            @RequestBody @Valid UserRequest request,
            BindingResult bindingResult
    ) {
        logger.info("Received updateUser request for userId={}", userId);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            logger.warn("Validation failed: {}", errorMessage);
            return new ResponseEntity<>(ApiResponse.error(ErrorCode.VALIDATION_FAILED, errorMessage), HttpStatus.BAD_REQUEST);
        }
        ApiResponse<UserResponse> response = userService.updateUser(userId, request, bindingResult);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID userId) {
        logger.info("Received deleteUser request for userId={}", userId);
        ApiResponse<Void> response = userService.deleteUser(userId);
        return new ResponseEntity<>(response, response.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}