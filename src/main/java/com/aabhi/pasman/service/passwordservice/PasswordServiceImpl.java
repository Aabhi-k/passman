package com.aabhi.pasman.service.passwordservice;

import com.aabhi.pasman.dto.password.PasswordDto;
import com.aabhi.pasman.repository.PasswordEntryRepository;
import io.jsonwebtoken.security.Password;

public class PasswordServiceImpl implements PasswordService {
    private final PasswordEntryRepository passwordEntryRepository;

    public PasswordServiceImpl(PasswordEntryRepository passwordEntryRepository) {
        this.passwordEntryRepository = passwordEntryRepository;
    }


    @Override
    public String insertPassword(PasswordDto passwordDto) {

        return "";
    }

    @Override
    public String getPassword(String passwordId) {
        return "";
    }
}
