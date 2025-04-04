package com.aabhi.pasman.service.passwordservice;

import com.aabhi.pasman.dto.password.PasswordDto;

public interface PasswordService {
    void insertPassword(PasswordDto passwordDto) throws Exception;
    PasswordDto getPassword(String passwordId) throws Exception;
    String updatePassword();
    String deletePassword();
    String getAllPasswords();
    String getPasswordById();

}
