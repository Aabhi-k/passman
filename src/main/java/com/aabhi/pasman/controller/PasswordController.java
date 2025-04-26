package com.aabhi.pasman.controller;

import com.aabhi.pasman.dto.password.PasswordDto;
import com.aabhi.pasman.dto.password.PasswordResponseDto;
import com.aabhi.pasman.service.passwordservice.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passwords")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertPassword(@RequestBody PasswordDto passwordDto) {
        try {
            passwordService.insertPassword(passwordDto);
            return ResponseEntity.ok("Password inserted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error inserting password: " + e.getMessage());
        }
    }

    @GetMapping("/getall/{userId}")
    public ResponseEntity<List<PasswordResponseDto>> getAllPasswords(@PathVariable String userId) {
       try {
              List<PasswordResponseDto> passwordDto = passwordService.getAllPasswords(userId);
              return ResponseEntity.ok(passwordDto);
         } catch (Exception e) {
              return ResponseEntity.status(500).body(null);
       }
    }

    @GetMapping("/get/{passwordId}")
    public ResponseEntity<PasswordResponseDto> getPassword(@PathVariable String passwordId) {
        try{
            PasswordResponseDto passwordDto = passwordService.getPassword(passwordId);
            return ResponseEntity.ok(passwordDto);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/update/{passwordId}")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordDto passwordDto, @PathVariable String passwordId) {
        try {
            passwordService.updatePassword(passwordDto, passwordId);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating password: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{passwordId}")
    public ResponseEntity<String> deletePassword(@PathVariable String passwordId) {
        try {
            String response = passwordService.deletePassword(passwordId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting password: " + e.getMessage());
        }
    }


}
