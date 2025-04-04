package com.aabhi.pasman.controller;

import com.aabhi.pasman.dto.password.InsertPasswordDto;
import com.aabhi.pasman.service.passwordservice.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPassword(@RequestBody InsertPasswordDto passwordDto) throws Exception {
        // Logic to add a password
        return ResponseEntity.ok(passwordService.insertPassword(passwordDto));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<InsertPasswordDto> getPassword(@PathVariable String id) throws Exception {
        // Logic to get a password
        Long passwordId = Long.parseLong(id);
        return ResponseEntity.ok(passwordService.getPassword(passwordId));
    }

    @PostMapping("/update")
    public ResponseEntity<String> updatePassword(@RequestBody InsertPasswordDto passwordDto) {
        // Logic to update a password
        return ResponseEntity.ok(passwordService.updatePassword());
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deletePassword(@RequestBody Long passwordId) {
        // Logic to delete a password
        return ResponseEntity.ok(passwordService.deletePassword());
    }

    @PostMapping("/all")
    public ResponseEntity<String> getAllPasswords() {
        // Logic to get all passwords
        return ResponseEntity.ok(passwordService.getAllPasswords());
    }

}
