package com.aabhi.pasman.controller;

import com.aabhi.pasman.dto.user.LoginDto;
import com.aabhi.pasman.dto.user.LoginResponseDto;
import com.aabhi.pasman.dto.user.UserDto;
import com.aabhi.pasman.service.authservice.AuthServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    public AuthController(AuthServiceImpl authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto){
        LoginResponseDto response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDto> register(@RequestBody UserDto userDto){
        LoginResponseDto lr = authService.register(userDto);
        if (lr == null) {
            return ResponseEntity.badRequest().body(null);
        }
        else {
            return ResponseEntity.ok(lr);
        }
    }

}
