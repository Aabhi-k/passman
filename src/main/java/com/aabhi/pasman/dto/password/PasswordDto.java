package com.aabhi.pasman.dto.password;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PasswordDto {
    private String id;
    private String encryptedData;
    private String iv;
    private String authTag;
    private String userId;
    private Long createdAt;
    private Long updatedAt;
}
