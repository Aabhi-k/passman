package com.aabhi.pasman.service.passwordservice;

import com.aabhi.pasman.dto.password.PasswordDto;

public interface PasswordService {
    String insertPassword(PasswordDto passwordDto) throws Exception;
    PasswordDto getPassword(Long passwordId);
    String updatePassword();
    String deletePassword();
    String getAllPasswords();
    String getPasswordById();

}
