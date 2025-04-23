package com.example.sem4_project.controller;

import com.example.sem4_project.dto.request.PositionRequest;
import com.example.sem4_project.dto.response.ApiResponse;
import com.example.sem4_project.dto.response.PositionResponse;
import com.example.sem4_project.exception.ErrorCode;
import com.example.sem4_project.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping
    public ApiResponse<List<PositionResponse>> getPositions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status
    ) {
        List<PositionResponse> positions = positionService.getPositions(page, size, status);
        return ApiResponse.success(ErrorCode.SUCCESS, positions);
    }


    @PostMapping
    public ApiResponse<PositionResponse> addPosition(@RequestBody PositionRequest request) {
        try {
            return positionService.addPosition(request);
        } catch (Exception e) {
            return ApiResponse.success(ErrorCode.OPERATION_FAILED);
        }
    }

    @PutMapping("/{positionId}")
    public ApiResponse<PositionResponse> updatePosition(
            @PathVariable UUID positionId,
            @RequestBody PositionRequest request
    ) {
        try {
            return positionService.updatePosition(positionId, request);
        } catch (Exception e) {
            return ApiResponse.success(ErrorCode.OPERATION_FAILED);
        }
    }

    @DeleteMapping("/{positionId}")
    public ApiResponse<Void> deletePosition(@PathVariable UUID positionId) {
        try {
            return positionService.deletePosition(positionId);
        } catch (Exception e) {
            return ApiResponse.success(ErrorCode.OPERATION_FAILED);
        }
    }

}
