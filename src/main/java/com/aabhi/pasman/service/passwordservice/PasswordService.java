package com.aabhi.pasman.service.passwordservice;

import com.aabhi.pasman.dto.password.InsertPasswordDto;
import com.aabhi.pasman.dto.password.ReturnPasswordDto;

public interface PasswordService {
    String insertPassword(InsertPasswordDto passwordDto) throws Exception;
    ReturnPasswordDto getPassword(Long passwordId) throws Exception;
    String updatePassword();
    String deletePassword();
    String getAllPasswords();
    String getPasswordById();

}
