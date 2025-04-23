package com.example.sem4_project.service;

import com.example.sem4_project.dto.request.DepartmentRequest;
import com.example.sem4_project.dto.response.DepartmentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DepartmentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Lấy danh sách phòng ban với phân trang và trạng thái
    public List<DepartmentResponse> getDepartments(int page, int size, String status) {
        int offset = (page - 1) * size;

        String baseQuery = "SELECT department_id, department_name, status FROM departments";
        List<Object> params = new ArrayList<>();

        // Thêm điều kiện lọc trạng thái nếu có
        if (status != null && (status.equalsIgnoreCase("Active") || status.equalsIgnoreCase("Inactive"))) {
            baseQuery += " WHERE status = ?";
            params.add(status);
        }

        // Thêm phân trang vào câu lệnh SQL
        baseQuery += " LIMIT ? OFFSET ?";
        params.add(size);
        params.add(offset);

        return jdbcTemplate.query(
                baseQuery,
                params.toArray(),
                (rs, rowNum) -> mapToDepartmentResponse(rs)
        );
    }

    // Thêm phòng ban và trả về phòng ban vừa thêm
    public DepartmentResponse addDepartment(DepartmentRequest request) {
        jdbcTemplate.update("CALL sp_add_department(?)", request.getDepartmentName());
        return getDepartmentByName(request.getDepartmentName());
    }

    // Cập nhật phòng ban và trả về phòng ban đã cập nhật
    public DepartmentResponse updateDepartment(UUID id, DepartmentRequest request) {
        jdbcTemplate.update("CALL sp_update_department(?, ?)", id.toString(), request.getDepartmentName());
        return getDepartmentById(id);
    }

    // Xóa phòng ban
    public void deleteDepartment(UUID id) {
        jdbcTemplate.update("CALL sp_delete_department(?)", id.toString());
    }

    // Lấy phòng ban theo tên
    private DepartmentResponse getDepartmentByName(String departmentName) {
        return jdbcTemplate.queryForObject(
                "SELECT department_id, department_name, status FROM departments WHERE department_name = ?",
                (rs, rowNum) -> mapToDepartmentResponse(rs),
                departmentName
        );
    }

    // Lấy phòng ban theo ID
    private DepartmentResponse getDepartmentById(UUID departmentId) {
        return jdbcTemplate.queryForObject(
                "SELECT department_id, department_name, status FROM departments WHERE department_id = ?",
                (rs, rowNum) -> mapToDepartmentResponse(rs),
                departmentId.toString()
        );
    }

    // Map kết quả từ ResultSet thành đối tượng DepartmentResponse
    private DepartmentResponse mapToDepartmentResponse(ResultSet rs) throws SQLException {
        DepartmentResponse response = new DepartmentResponse();
        response.setDepartmentId(UUID.fromString(rs.getString("department_id")));
        response.setDepartmentName(rs.getString("department_name"));
        response.setStatus(rs.getString("status"));
        return response;
    }
}
