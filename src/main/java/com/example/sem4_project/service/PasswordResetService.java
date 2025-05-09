package com.example.sem4_project.service;

import com.example.sem4_project.dto.request.ForgotPasswordRequest;
import com.example.sem4_project.dto.request.ResetPasswordRequest;
import com.example.sem4_project.exception.ErrorCode;
import com.example.sem4_project.dto.response.ApiResponse;
import com.example.sem4_project.entity.User;
import com.example.sem4_project.repository.UserRepository;
import com.example.sem4_project.util.EmailSender;
import com.example.sem4_project.util.RedisUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final RedisUtil redisUtil;
    private final EmailSender emailSender;
    private final BCryptPasswordEncoder passwordEncoder;

    // 1. Gửi OTP
    @Transactional
    public ApiResponse sendOtpToEmail(ForgotPasswordRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return ApiResponse.error(ErrorCode.USER_NOT_FOUND, "Email không tồn tại trong hệ thống.");
        }

        String otpCode = generateOtp();
        redisUtil.storeOtpInRedis(request.getEmail(), otpCode);
        emailSender.sendOtpEmail(request.getEmail(), otpCode);

        return ApiResponse.success(ErrorCode.OTP_SENT_SUCCESSFULLY);
    }

    // 2. Xác thực OTP
    public ApiResponse verifyOtp(String email, String otpCode) {
        String storedOtp = redisUtil.getOtpFromRedis(email);
        if (storedOtp == null) {
            return ApiResponse.error(ErrorCode.OTP_EXPIRED, "Mã OTP đã hết hạn.");
        }

        if (!storedOtp.equals(otpCode)) {
            return ApiResponse.error(ErrorCode.INVALID_OTP, "Mã OTP không đúng.");
        }

        redisUtil.storeOtpInRedis(email + "_verified", "true");
        return ApiResponse.success(ErrorCode.OTP_VERIFIED_SUCCESSFULLY);
    }

    @PersistenceContext
    private EntityManager entityManager;

    // 3. Đặt lại mật khẩu
    @Transactional
    public ApiResponse resetPassword(String email, ResetPasswordRequest resetRequest) {
        String isVerified = redisUtil.getOtpFromRedis(email + "_verified");
        if (!"true".equals(isVerified)) {
            return ApiResponse.error(ErrorCode.UNAUTHORIZED, "Bạn chưa xác thực OTP hoặc OTP đã hết hạn.");
        }

        if (!resetRequest.getNewPassword().equals(resetRequest.getConfirmPassword())) {
            return ApiResponse.error(ErrorCode.INVALID_INPUT, "Mật khẩu xác nhận không khớp.");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ApiResponse.error(ErrorCode.USER_NOT_FOUND, "Không tìm thấy người dùng với email này.");
        }

        User user = userOpt.get();

        // Gắn kết lại entity với persistence context
        User managedUser = entityManager.find(User.class, user.getUserId());

        // Nếu managedUser là null => đã bị xóa ở nơi khác
        if (managedUser == null) {
            return ApiResponse.error(ErrorCode.USER_NOT_FOUND, "Người dùng đã bị xóa trong lúc xử lý.");
        }

        String encodedPassword = passwordEncoder.encode(resetRequest.getNewPassword());
        managedUser.setPassword(encodedPassword);

        // Không cần gọi save() vì managedUser đã gắn kết, sẽ tự động được cập nhật khi kết thúc @Transactional

        redisUtil.deleteOtpFromRedis(email);
        redisUtil.deleteOtpFromRedis(email + "_verified");

        return ApiResponse.success(ErrorCode.PASSWORD_UPDATED_SUCCESSFULLY);
    }
    // Helper tạo OTP
    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6 số
        return String.valueOf(otp);
    }
}
