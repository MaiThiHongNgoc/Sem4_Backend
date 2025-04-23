package com.example.sem4_project.service;

import com.example.sem4_project.dto.request.PositionRequest;
import com.example.sem4_project.dto.response.ApiResponse;
import com.example.sem4_project.dto.response.PositionResponse;
import com.example.sem4_project.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PositionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<PositionResponse> getPositions(int page, int size, String status) {
        String query = "CALL sp_get_all_positions()";

        List<PositionResponse> all = jdbcTemplate.query(
                query,
                (rs, rowNum) -> new PositionResponse(
                        UUID.fromString(rs.getString("position_id")),
                        rs.getString("position_name"),
                        rs.getString("status")
                )
        );

        if (status != null && (status.equalsIgnoreCase("Active") || status.equalsIgnoreCase("Inactive"))) {
            return all.stream()
                    .filter(p -> p.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }

        return all;
    }

    // Thêm mới Chức Vụ
    public ApiResponse<PositionResponse> addPosition(PositionRequest request) {
        jdbcTemplate.update(
                "CALL sp_add_position(?)",
                request.getPositionName()
        );
        return ApiResponse.success(ErrorCode.SUCCESS, new PositionResponse(UUID.randomUUID(), request.getPositionName(), "Active"));
    }

    // Cập nhật Chức Vụ
    public ApiResponse<PositionResponse> updatePosition(UUID positionId, PositionRequest request) {
        jdbcTemplate.update(
                "CALL sp_update_position(?, ?)",
                positionId.toString(),
                request.getPositionName()
        );
        return ApiResponse.success(ErrorCode.SUCCESS, new PositionResponse(positionId, request.getPositionName(), "Active"));
    }

    // Xóa Chức Vụ (set trạng thái thành Inactive)
    public ApiResponse<Void> deletePosition(UUID positionId) {
        jdbcTemplate.update(
                "CALL sp_delete_position(?)",
                positionId.toString()
        );
        return ApiResponse.success(ErrorCode.SUCCESS);
    }
}
