package com.tokengate.tokengate.service;

import com.tokengate.tokengate.dto.LoginDTO;
import com.tokengate.tokengate.dto.UserDTO;
import com.tokengate.tokengate.model.Users;
import com.tokengate.tokengate.repository.UserRepository;
import com.tokengate.tokengate.utils.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired private JavaMailSender mailSender;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ModelMapper modelMapper;
    @Autowired private EmailService emailService;
    @Autowired private JwtUtil jwtUtil;

    public void register(UserDTO userDTO) {
        Users user = modelMapper.map(userDTO, Users.class);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setVerificationToken(UUID.randomUUID().toString());
        user.setVerified(false);

        userRepository.save(user);

        // Send verification email
        sendVerificationEmail(user);
    }

    public void forgotPassword(String email){
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("TokenGate - Password Reset");
        message.setText("Hello " + user.getUsername() + ",\n\n"
                + "Click the link below to set a new password :\n"
                + resetLink + "\n\n"
                + "If you did not request for password reset, ignore this email.");

        mailSender.send(message);
        System.out.println("Reset Password email sent to: " + user.getEmail());

    }

    private void sendVerificationEmail(Users user) {
        String link = "http://localhost:5173/verify?token=" + user.getVerificationToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("TokenGate - Email Verification");
        message.setText("Hello " + user.getUsername() + ",\n\n"
                + "Click the link below to verify your email:\n"
                + link + "\n\n"
                + "If you did not signup, ignore this email.");

        mailSender.send(message);
        System.out.println("Verification email sent to: " + user.getEmail());
    }

    public void verifyEmail(String token) {
        Users user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    public void resetPassword(String token, String newPassword) {

        Users user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }

    public String login(LoginDTO loginDTO) {
        Users user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isVerified()) {
            throw new RuntimeException("Email not verified");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
