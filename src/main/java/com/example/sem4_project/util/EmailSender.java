package com.example.sem4_project.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    private final JavaMailSender javaMailSender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendOtpEmail(String toEmail, String otpCode) {
        // Tạo thông điệp email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Mã xác thực OTP");
        message.setText("Mã OTP của bạn là: " + otpCode + "\nLưu ý: Mã OTP này sẽ hết hạn sau 10 phút.");

        // Gửi email
        javaMailSender.send(message);
    }
}
