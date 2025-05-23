package com.example.sem4_project.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeResponse {
    UUID employeeId;
    String fullName;
    String gender;
    LocalDate dateOfBirth;
    String phone;
    String email;
    String address;
    UUID departmentId;
    UUID positionId;
    LocalDate hireDate;
    String status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
