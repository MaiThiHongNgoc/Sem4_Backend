package com.example.sem4_project.service;

import com.example.sem4_project.dto.request.LoginRequest;
import com.example.sem4_project.dto.response.LoginResponse;
import com.example.sem4_project.entity.User;
import com.example.sem4_project.exception.ErrorCode;
import com.example.sem4_project.repository.UserRepository;
import com.example.sem4_project.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        logger.debug("Attempting login for username: {}", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        logger.debug("Authentication successful for username: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    logger.error("User not found: {}", request.getUsername());
                    return new RuntimeException(ErrorCode.USER_NOT_FOUND.getMessage());
                });

        if (user.getStatus() != User.Status.Active) {
            logger.error("Account disabled for username: {}", request.getUsername());
            throw new RuntimeException("Tài khoản đã bị vô hiệu hóa.");
        }

        String roleName = user.getRole() != null ? user.getRole().getRole_name() : "USER";
        logger.debug("Role for username {}: {}", request.getUsername(), roleName);

        String token = jwtTokenProvider.createToken(user.getUsername(), roleName);
        logger.debug("Generated JWT token for username: {}", request.getUsername());

        return new LoginResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().getRole_id().toString() : null,
                user.getStatus() != null ? user.getStatus().name() : null,
                token
        );
    }
}
