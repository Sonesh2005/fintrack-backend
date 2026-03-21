package com.sonesh.finance.controller;

import com.sonesh.finance.dto.AuthResponse;
import com.sonesh.finance.dto.ForgotPasswordRequest;
import com.sonesh.finance.dto.LoginRequest;
import com.sonesh.finance.dto.MessageResponse;
import com.sonesh.finance.dto.RegisterRequest;
import com.sonesh.finance.dto.ResetPasswordRequest;
import com.sonesh.finance.model.User;
import com.sonesh.finance.security.JwtService;
import com.sonesh.finance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email, req.password)
        );

        String token = jwtService.generateToken(req.email);

        // 🔥 fetch user details
        User user = userService.getUserByEmail(req.email);

        return ResponseEntity.ok(
                new AuthResponse(
                        token,
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.sendResetPasswordLink(request.getEmail());
        return ResponseEntity.ok(new MessageResponse("Password reset link sent to your email"));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(
                request.getToken(),
                request.getNewPassword(),
                request.getConfirmPassword()
        );
        return ResponseEntity.ok(new MessageResponse("Password reset successfully"));
    }
}