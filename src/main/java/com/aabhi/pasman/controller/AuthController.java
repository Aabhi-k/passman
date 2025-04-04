package com.aabhi.pasman.controller;

import com.aabhi.pasman.dto.user.LoginDto;
import com.aabhi.pasman.dto.user.LoginResponse;
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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto){
        LoginResponse response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto userDto){
        String token = authService.register(userDto);
        if (token.equals("null")) {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
        else {
            return ResponseEntity.ok(token);
        }
    }

//    @PostMapping("/change-password")
//    public ResponseEntity<String> changePassword(@RequestBody UserDto userDto){
//        authService.changePassword(userDto);
//        return ResponseEntity.ok("Password changed");
//    }



}
