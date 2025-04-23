package com.example.sem4_project.dto.response;

import com.example.sem4_project.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    int code;
    String message;
    T result;

    public static <T> ApiResponse<T> success(ErrorCode errorCode, T result) {
        return ApiResponse.<T>builder()
                .code(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .result(result)
                .build();
    }

    public static <T> ApiResponse<T> success(ErrorCode code) {
        return success(code, null);
    }
}
