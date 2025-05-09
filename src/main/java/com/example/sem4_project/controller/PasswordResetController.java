package com.example.sem4_project.controller;

import com.example.sem4_project.dto.request.EnterOtpRequest;
import com.example.sem4_project.dto.request.ForgotPasswordRequest;
import com.example.sem4_project.dto.request.ResetPasswordRequest;
import com.example.sem4_project.dto.response.ApiResponse;
import com.example.sem4_project.service.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // 1. Gửi mã OTP tới email
    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtpToEmail(@RequestBody @Valid ForgotPasswordRequest request) {
        return passwordResetService.sendOtpToEmail(request);
    }

    // 2. Xác nhận mã OTP
    @PostMapping("/verify-otp/{email}")
    public ApiResponse<Void> verifyOtp(
            @PathVariable String email,
            @RequestBody @Valid EnterOtpRequest otpRequest) {
        return passwordResetService.verifyOtp(email, otpRequest.getOtpCode());
    }

    // 3. Đặt lại mật khẩu
    @PostMapping("/reset-password/{email}")
    public ApiResponse<Void> resetPassword(
            @PathVariable String email,
            @RequestBody @Valid ResetPasswordRequest resetRequest) {
        return passwordResetService.resetPassword(email, resetRequest);
    }
}
