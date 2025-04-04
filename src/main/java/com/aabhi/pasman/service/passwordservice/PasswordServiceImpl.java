package com.aabhi.pasman.service.passwordservice;

import com.aabhi.pasman.dto.password.PasswordDto;
import com.aabhi.pasman.model.Password;
import com.aabhi.pasman.repository.PasswordRepository;
import com.aabhi.pasman.service.authservice.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final PasswordRepository passwordRepository;
    private final AuthService authService;


    @Autowired
    public PasswordServiceImpl(PasswordRepository passwordRepository, AuthService authService) {
        this.passwordRepository = passwordRepository;
        this.authService = authService;
    }


    @Override
    public void insertPassword(PasswordDto passwordDto) throws Exception {
       String userId = passwordDto.getUserId();
       if (userId == null) {
           throw new Exception("User ID is null");
       } else if (!authService.checkUser(userId)) {
           throw new Exception("User not found");
       }
       Password password = toPassword(passwordDto);

        passwordRepository.save(password);

    }



    @Override
    public PasswordDto getPassword(String passwordId) throws Exception {
        return null;
    }

    @Override
    public String updatePassword() {
        return "";
    }

    @Override
    public String deletePassword() {
        return "";
    }

    @Override
    public String getAllPasswords() {
        return "";
    }

    @Override
    public String getPasswordById() {
        return "";
    }

    // Mappers
    public PasswordDto toPasswordDto(Password password) {
        return PasswordDto.builder()
                .id(password.getId())
                .encryptedData(password.getEncryptedData())
                .iv(password.getIv())
                .authTag(password.getAuthTag())
                .userId(password.getUser().getId())
                .createdAt(password.getCreatedAt())
                .updatedAt(password.getUpdatedAt())
                .build();
    }
    public Password toPassword(PasswordDto passwordDto) {
        return Password.builder()
                .encryptedData(passwordDto.getEncryptedData())
                .iv(passwordDto.getIv())
                .authTag(passwordDto.getAuthTag())
                .user(authService.getUserById(passwordDto.getUserId()))
                .createdAt(new Date().getTime())
                .updatedAt(new Date().getTime())
                .build();
    }

}


