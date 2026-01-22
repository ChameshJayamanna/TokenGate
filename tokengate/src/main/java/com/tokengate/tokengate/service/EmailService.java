package com.tokengate.tokengate.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void sendVerificationEmail(String email, String token) {
        String link = "http://localhost:3000/verify?token=" + token;
        System.out.println("Verification link for " + email + ": " + link);
    }
}
