package com.aabhi.pasman.service.passwordservice;

import com.aabhi.pasman.dto.password.PasswordDto;

public interface PasswordService {
    String insertPassword(PasswordDto passwordDto);
    String getPassword(String passwordId);

}
