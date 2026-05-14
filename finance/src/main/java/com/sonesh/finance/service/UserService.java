package com.sonesh.finance.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sonesh.finance.dto.RegisterRequest;
import com.sonesh.finance.model.User;
import com.sonesh.finance.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public User register(RegisterRequest request) {
        String name = request.getName() == null ? "" : request.getName().trim();
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();
        String password = request.getPassword() == null ? "" : request.getPassword().trim();
        String confirmPassword = request.getConfirmPassword() == null ? "" : request.getConfirmPassword().trim();

        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        String verificationToken = UUID.randomUUID().toString();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmailVerified(false);
        user.setVerificationToken(verificationToken);
        user.setVerificationTokenExpiry(LocalDateTime.now().plusMinutes(30));

        User savedUser = userRepository.save(user);

        sendVerificationEmail(savedUser);

        return savedUser;
    }

    private void sendVerificationEmail(User user) {
        String verifyLink = frontendUrl + "/verify-email?token=" + user.getVerificationToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Verify your FinTrack account");
        message.setText(
                "Hello " + user.getName() + ",\n\n" +
                        "Welcome to FinTrack.\n\n" +
                        "Please verify your email by clicking the link below:\n" +
                        verifyLink + "\n\n" +
                        "This link will expire in 30 minutes.\n\n" +
                        "If you did not create this account, please ignore this email."
        );

        mailSender.send(message);
    }

    public void verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);

        if (user == null) {
            return;
        }

        if (user.getVerificationTokenExpiry() == null ||
                user.getVerificationTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token expired");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setVerificationTokenExpiry(null);

        userRepository.save(user);
    }

    public void sendResetPasswordLink(String email) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        String resetLink = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Reset your FinTrack password");
        message.setText(
                "Hello " + user.getName() + ",\n\n" +
                        "Click the link below to reset your password:\n" +
                        resetLink + "\n\n" +
                        "This link will expire in 15 minutes.\n\n" +
                        "If you did not request this, please ignore this email."
        );

        mailSender.send(message);
    }

    public void resetPassword(String token, String newPassword, String confirmPassword) {
        String password = newPassword == null ? "" : newPassword.trim();
        String confirm = confirmPassword == null ? "" : confirmPassword.trim();

        if (!password.equals(confirm)) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }

    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public void sendLoginOtp(String email) {

        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Generate 6-digit OTP
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        // ✅ Save OTP + expiry
        user.setLoginOtp(otp);
        user.setLoginOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        // ✅ Send email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Your FinTrack Login OTP");

        message.setText(
                "Hello " + user.getName() + ",\n\n" +
                        "Your OTP for FinTrack login is:\n\n" +
                        otp + "\n\n" +
                        "This OTP will expire in 5 minutes.\n\n" +
                        "If you did not request this login, please ignore this email."
        );

        mailSender.send(message);
    }
    public User verifyLoginOtp(String email, String otp) {

        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Check OTP
        if (user.getLoginOtp() == null || !user.getLoginOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        // ✅ Check expiry
        if (user.getLoginOtpExpiry() == null ||
                user.getLoginOtpExpiry().isBefore(LocalDateTime.now())) {

            throw new RuntimeException("OTP expired");
        }

        // ✅ Clear OTP after successful verification
        user.setLoginOtp(null);
        user.setLoginOtpExpiry(null);

        userRepository.save(user);

        return user;
    }

}