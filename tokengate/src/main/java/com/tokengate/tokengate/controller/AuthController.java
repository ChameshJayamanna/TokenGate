package com.tokengate.tokengate.controller;

import com.tokengate.tokengate.dto.LoginDTO;
import com.tokengate.tokengate.dto.UserDTO;
import com.tokengate.tokengate.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserDTO userDTO) {
        authService.register(userDTO);
        return ResponseEntity.ok("Check console for verification link!");
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email verified successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        String jwt = authService.login(loginDTO);
        return ResponseEntity.ok(jwt);
    }
}
