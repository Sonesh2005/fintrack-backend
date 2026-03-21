package com.sonesh.finance.service;

import com.sonesh.finance.dto.RegisterRequest;
import com.sonesh.finance.model.User;
import com.sonesh.finance.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    public User register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public void sendResetPasswordLink(String email) {
        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
        userRepository.save(user);

        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        String htmlContent = buildResetPasswordEmail(user.getName(), resetLink);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("fintrack.app.official@gmail.com", "FinTrack");
            helper.setTo(user.getEmail());
            helper.setSubject("Reset your FinTrack password");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to prepare email", e);
        }
    }

    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }

    private String buildResetPasswordEmail(String name, String resetLink) {

        return """
        <!DOCTYPE html>
        <html lang="en">
          <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <title>Reset your FinTrack password</title>
          </head>

          <body style="margin:0;padding:0;background:#0b1020;font-family:Arial,Helvetica,sans-serif;color:#ffffff;">
            <table width="100%%" style="padding:32px 0;background:#0b1020;">
              <tr>
                <td align="center">

                  <table width="100%%" style="max-width:640px;background:#11182f;border-radius:24px;border:1px solid rgba(255,255,255,0.08);overflow:hidden;">

                    <!-- HEADER -->
                    <tr>
                      <td style="padding:32px;">
                        <table>
                          <tr>

                            <!-- LOGO (NO IMAGE NEEDED) -->
                            <td style="width:56px;height:56px;border-radius:16px;background:linear-gradient(135deg,#22c55e,#14b8a6,#0ea5e9);text-align:center;font-size:20px;font-weight:800;color:white;">
                              FT
                            </td>

                            <td style="padding-left:14px;">
                              <div style="font-size:24px;font-weight:700;">FinTrack</div>
                              <div style="font-size:12px;color:#94a3b8;">Premium Finance OS</div>
                            </td>

                          </tr>
                        </table>

                        <div style="margin-top:24px;font-size:12px;color:#67e8f9;font-weight:600;">
                          PASSWORD SECURITY
                        </div>

                        <h1 style="margin:10px 0;font-size:28px;">Reset your password</h1>

                        <p style="color:#cbd5f5;font-size:14px;">
                          Hello %s,<br/><br/>
                          We received a request to reset your FinTrack password.
                        </p>
                      </td>
                    </tr>

                    <!-- BUTTON -->
                    <tr>
                      <td align="center" style="padding:20px;">
                        <a href="%s"
                           style="padding:14px 28px;background:linear-gradient(90deg,#22c55e,#0ea5e9);
                                  border-radius:12px;color:white;text-decoration:none;font-weight:bold;">
                          Reset Password
                        </a>
                      </td>
                    </tr>

                    <!-- LINK -->
                    <tr>
                      <td style="padding:0 32px 20px 32px;">
                        <div style="font-size:12px;color:#94a3b8;">
                          Or copy this link:
                        </div>
                        <div style="margin-top:8px;font-size:12px;color:#60a5fa;">
                          %s
                        </div>
                      </td>
                    </tr>

                    <!-- FOOTER -->
                    <tr>
                      <td style="padding:24px;text-align:center;font-size:12px;color:#64748b;">
                        FinTrack • Secure Finance System
                      </td>
                    </tr>

                  </table>

                </td>
              </tr>
            </table>
          </body>
        </html>
        """.formatted(name, resetLink, resetLink);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}