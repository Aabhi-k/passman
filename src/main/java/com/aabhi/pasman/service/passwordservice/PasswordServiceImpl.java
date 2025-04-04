package com.aabhi.pasman.service.passwordservice;

import com.aabhi.pasman.dto.password.InsertPasswordDto;
import com.aabhi.pasman.dto.password.ReturnPasswordDto;
import com.aabhi.pasman.model.Password;
import com.aabhi.pasman.repository.PasswordRepository;
import com.aabhi.pasman.security.encryption.EncryptionUtil;
import com.aabhi.pasman.service.authservice.AuthService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final PasswordRepository passwordRepository;
    private final EncryptionUtil encryptionUtil;
    private final AuthService authService;


    public PasswordServiceImpl(PasswordRepository passwordRepository, EncryptionUtil encryptionUtil, AuthService authService) {
        this.passwordRepository = passwordRepository;
        this.encryptionUtil = encryptionUtil;
        this.authService = authService;
    }


    @Override
    public String insertPassword(InsertPasswordDto passwordDto) throws Exception {
        Long userId = passwordDto.getUserId();
        if (userId == null) {
            throw new IllegalStateException("User not authenticated");
        } else if (!authService.checkUser(userId)) {
            throw new IllegalStateException("User not found");
        }

        InsertPasswordDto encryptedPasswordDto = encryptionUtil.encryptPassword(passwordDto);
        Password password = toPassword(encryptedPasswordDto);
        password.setUser(authService.getUserById(userId));
        password.setCreatedAt(new Date());
        password.setUpdatedAt(new Date());

        // Save the password to the database
        passwordRepository.save(password);
        // Return the ID of the saved password
        return password.getId().toString();
    }



    @Override
    public ReturnPasswordDto getPassword(Long passwordId) throws Exception {
        Password encryptedPassword = passwordRepository.findById(passwordId)
                .orElseThrow(() -> new IllegalStateException("Password not found"));

        // Decrypt the password
        return encryptionUtil.decryptPassword(toInsertPasswordDto(encryptedPassword));
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
    public InsertPasswordDto toInsertPasswordDto(Password password) {
        return new InsertPasswordDto(
                password.getUsername(),
                password.getPassword(),
                password.getUrl(),
                password.getDescription(),
                password.getUser().getId()
        );
    }
    public Password toPassword(InsertPasswordDto passwordDto) {
        return new Password(
                passwordDto.getUsername(),
                passwordDto.getPassword(),
                passwordDto.getUrl(),
                passwordDto.getDescription()
        );
    }

}


