package com.example.sem4_project.controller;

import com.example.sem4_project.dto.request.DepartmentRequest;
import com.example.sem4_project.dto.response.ApiResponse;
import com.example.sem4_project.dto.response.DepartmentResponse;
import com.example.sem4_project.exception.ErrorCode;
import com.example.sem4_project.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public ApiResponse<List<DepartmentResponse>> getDepartmentsWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status
    ) {
        List<DepartmentResponse> departments = departmentService.getDepartments(page, size, status);
        return ApiResponse.success(ErrorCode.SUCCESS, departments);
    }

    @PostMapping
    public ApiResponse<DepartmentResponse> addDepartment(@RequestBody DepartmentRequest request) {
        DepartmentResponse departmentResponse = departmentService.addDepartment(request);
        return ApiResponse.success(ErrorCode.SUCCESS, departmentResponse);
    }

    @PutMapping("/{id}")
    public ApiResponse<DepartmentResponse> updateDepartment(
            @PathVariable UUID id,
            @RequestBody DepartmentRequest request
    ) {
        DepartmentResponse updatedDepartment = departmentService.updateDepartment(id, request);
        return ApiResponse.success(ErrorCode.SUCCESS, updatedDepartment);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ApiResponse.success(ErrorCode.SUCCESS);
    }
}
