package com.aabhi.pasman.service.passwordservice;

import com.aabhi.pasman.dto.password.PasswordDto;
import com.aabhi.pasman.dto.password.PasswordResponseDto;

import java.util.List;

public interface PasswordService {
    void insertPassword(PasswordDto passwordDto) throws Exception;
    PasswordResponseDto getPassword(String passwordId) throws Exception;
    void updatePassword(PasswordDto passwordDto, String passwordId) throws Exception;
    String deletePassword(String passwordId) throws Exception;
    List<PasswordResponseDto> getAllPasswords(String userId);

}
