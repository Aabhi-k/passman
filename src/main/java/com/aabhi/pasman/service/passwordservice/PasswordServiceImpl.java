package com.aabhi.pasman.service.passwordservice;

import com.aabhi.pasman.dto.password.PasswordDto;
import com.aabhi.pasman.dto.password.PasswordResponseDto;
import com.aabhi.pasman.model.Password;
import com.aabhi.pasman.repository.PasswordRepository;
import com.aabhi.pasman.service.authservice.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public PasswordResponseDto getPassword(String passwordId) throws Exception {
        if (passwordId == null) {
            throw new Exception("Password ID is null");
        }
        Optional<Password> password = passwordRepository.findById(passwordId);
        if(password.isPresent()) {
            Password foundPassword = password.get();
            return PasswordResponseDto.builder()
                    .id(foundPassword.getId())
                    .encryptedData(foundPassword.getEncryptedData())
                    .iv(foundPassword.getIv())
                    .authTag(foundPassword.getAuthTag())
                    .userId(foundPassword.getUser().getId())
                    .createdAt(foundPassword.getCreatedAt().getTime())
                    .updatedAt(foundPassword.getUpdatedAt().getTime())
                    .build();
        }
        else {
            throw new Exception("Password not found");
        }
    }

    @Override
    public void updatePassword(PasswordDto passwordDto, String passwordId) throws Exception {
        Password password = passwordRepository.findById(passwordId).get();
        if (passwordDto.getEncryptedData() != null) {
            password.setEncryptedData(passwordDto.getEncryptedData());
        }
        if (passwordDto.getIv() != null) {
            password.setIv(passwordDto.getIv());
        }
        if (passwordDto.getAuthTag() != null) {
            password.setAuthTag(passwordDto.getAuthTag());
        }
        password.setUpdatedAt(new Date());
        passwordRepository.save(password);
    }

    @Override
    public String deletePassword(String passwordId) throws Exception {
        if(passwordId == null) {
            throw new Exception("Password ID is null");
        }
        Optional<Password> password = passwordRepository.findById(passwordId);
        if(password.isPresent()) {
            passwordRepository.deleteById(passwordId);
            return "Password deleted successfully";
        }
        else {
            throw new Exception("Password not found");
        }
    }

    @Override
    public List<PasswordResponseDto> getAllPasswords(String userId) {
        if (userId == null) {
            return null;
        } else if (!authService.checkUser(userId)) {
            return null;
        }

        List<Password> passwords = passwordRepository.findByUserId(userId);

        if (passwords.isEmpty()) {
            return null;
        }
        return passwords.stream()
                .map(this::toPasswordDto)
                .toList();

    }

    // Mappers
    public PasswordResponseDto toPasswordDto(Password password) {
        return PasswordResponseDto.builder()
                .id(password.getId())
                .encryptedData(password.getEncryptedData())
                .iv(password.getIv())
                .authTag(password.getAuthTag())
                .userId(password.getUser().getId())
                .createdAt(password.getCreatedAt().getTime())
                .updatedAt(password.getUpdatedAt().getTime())
                .build();
    }
    public Password toPassword(PasswordDto passwordDto) {
        return Password.builder()
                .encryptedData(passwordDto.getEncryptedData())
                .iv(passwordDto.getIv())
                .authTag(passwordDto.getAuthTag())
                .user(authService.getUserById(passwordDto.getUserId()))
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

}


