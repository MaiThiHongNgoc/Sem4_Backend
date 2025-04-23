package com.example.sem4_project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // ==== Thành công ====
    SUCCESS(HttpStatus.OK, "Thành công"),

    // ==== Người dùng ====
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email đã tồn tại"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "Tên đăng nhập đã tồn tại"),

    // ==== Xác thực / Đăng nhập ====
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Tên đăng nhập hoặc mật khẩu không đúng"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Chưa xác thực"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Không có quyền truy cập"),

    // ==== Phòng ban / Chức vụ ====
    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy phòng ban"),
    POSITION_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy chức vụ"),
    INVALID_DEPARTMENT_ID(HttpStatus.NOT_FOUND, "ID không hợp lệ"),

    // ==== Nhân viên ====
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy nhân viên"),
    EMPLOYEE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Nhân viên đã tồn tại"),
    EMPLOYEE_INACTIVE(HttpStatus.BAD_REQUEST, "Nhân viên đã nghỉ việc hoặc không hoạt động"),

    // ==== Lịch sử làm việc ====
    HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy lịch sử làm việc"),

    // ==== QR / Chấm công ====
    QR_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy mã QR"),
    QR_ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin chấm công QR"),
    DUPLICATE_CHECKIN(HttpStatus.BAD_REQUEST, "Bạn đã chấm công rồi, không thể chấm công lại"),

    // ==== Chấm công tổng hợp ====
    ATTENDANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin chấm công"),

    // ==== Nghỉ phép ====
    LEAVE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy yêu cầu nghỉ phép"),
    INVALID_LEAVE_DATE(HttpStatus.BAD_REQUEST, "Ngày nghỉ không hợp lệ"),
    LEAVE_OVERLAP(HttpStatus.CONFLICT, "Thời gian nghỉ bị trùng với lịch làm việc hoặc nghỉ khác"),

    // ==== Lịch làm việc ====
    WORK_SCHEDULE_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin lịch làm việc mẫu"),
    WORK_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy lịch làm việc"),
    INVALID_SCHEDULE_TIME(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu hoặc kết thúc không hợp lệ"),

    // ==== Xử lý chung ====
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ"),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "Không tìm thấy dữ liệu"),
    OPERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Thao tác thất bại");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
