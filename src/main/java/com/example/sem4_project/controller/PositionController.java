package com.example.sem4_project.controller;

import com.example.sem4_project.dto.request.PositionRequest;
import com.example.sem4_project.dto.response.ApiResponse;
import com.example.sem4_project.dto.response.PositionResponse;
import com.example.sem4_project.exception.ErrorCode;
import com.example.sem4_project.service.PositionService;
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
@RequestMapping("/api/positions")
public class PositionController {

    private static final Logger logger = LoggerFactory.getLogger(PositionController.class);

    @Autowired
    private PositionService positionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PositionResponse>>> getPositions(
            @RequestParam(required = false) String status
    ) {
        logger.info("Received getPositions request: status={}", status);
        List<PositionResponse> positions = positionService.getPositions(status);
        return new ResponseEntity<>(ApiResponse.success(ErrorCode.SUCCESS, positions), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PositionResponse>> addPosition(
            @RequestBody @Valid PositionRequest request,
            BindingResult bindingResult
    ) {
        logger.info("Received addPosition request: positionName={}", request.getPositionName());
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            logger.warn("Validation failed: {}", errorMessage);
            return new ResponseEntity<>(ApiResponse.error(ErrorCode.VALIDATION_FAILED, errorMessage), HttpStatus.BAD_REQUEST);
        }
        ApiResponse<PositionResponse> response = positionService.addPosition(request);
        HttpStatus status = response.getErrorCode() == ErrorCode.SUCCESS ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @PutMapping("/{positionId}")
    public ResponseEntity<ApiResponse<PositionResponse>> updatePosition(
            @PathVariable UUID positionId,
            @RequestBody @Valid PositionRequest request,
            BindingResult bindingResult
    ) {
        logger.info("Received updatePosition request for positionId={}", positionId);
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            logger.warn("Validation failed: {}", errorMessage);
            return new ResponseEntity<>(ApiResponse.error(ErrorCode.VALIDATION_FAILED, errorMessage), HttpStatus.BAD_REQUEST);
        }
        ApiResponse<PositionResponse> response = positionService.updatePosition(positionId, request);
        HttpStatus status = response.getErrorCode() == ErrorCode.SUCCESS ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @DeleteMapping("/{positionId}")
    public ResponseEntity<ApiResponse<Void>> deletePosition(@PathVariable UUID positionId) {
        logger.info("Received deletePosition request for positionId={}", positionId);
        ApiResponse<Void> response = positionService.deletePosition(positionId);
        HttpStatus status = response.getErrorCode() == ErrorCode.SUCCESS ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }
}