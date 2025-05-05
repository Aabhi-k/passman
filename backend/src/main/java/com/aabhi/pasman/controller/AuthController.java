package com.aabhi.pasman.controller;

import com.aabhi.pasman.dto.user.LoginDto;
import com.aabhi.pasman.dto.user.LoginResponseDto;
import com.aabhi.pasman.dto.user.UserDto;
import com.aabhi.pasman.service.authservice.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        LoginResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody UserDto userDto) {
        LoginResponseDto response = authService.register(userDto);
        return ResponseEntity.ok(response);
    }
}
