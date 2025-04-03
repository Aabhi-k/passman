package com.aabhi.pasman.security.encryption;

import com.aabhi.pasman.dto.password.PasswordDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class EncryptionUtil {
    private final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private final int IV_SIZE = 16;  // 128-bit IV

    @Value("${encryption.key}")
    private byte[] key;

    public String encrypt(String plainText) throws Exception {
        byte[] clean = plainText.getBytes("UTF-8");
        byte[] iv = new byte[IV_SIZE];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(clean);

        byte[] encryptedIVAndText = new byte[IV_SIZE + encrypted.length];
        System.arraycopy(iv, 0, encryptedIVAndText, 0, IV_SIZE);
        System.arraycopy(encrypted, 0, encryptedIVAndText, IV_SIZE, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedIVAndText);
    }



    public String decrypt(String encryptedText) throws Exception {
        byte[] encryptedIvTextBytes = Base64.getDecoder().decode(encryptedText);

        // Extract the IV from the beginning.
        byte[] iv = new byte[IV_SIZE];
        System.arraycopy(encryptedIvTextBytes, 0, iv, 0, IV_SIZE);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        // Extract the remaining encrypted part.
        int encryptedSize = encryptedIvTextBytes.length - IV_SIZE;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(encryptedIvTextBytes, IV_SIZE, encryptedBytes, 0, encryptedSize);

        // Create key specification.
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

        // Set up Cipher instance and decrypt.
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(encryptedBytes);

        return new String(decrypted, "UTF-8");
    }



    public PasswordDto encryptPassword(PasswordDto passwordDto) throws Exception {
        String encryptedPassword = encrypt(passwordDto.getPassword());

        String encryptedUsername = encrypt(passwordDto.getUsername());
        String encryptedUrl = encrypt(passwordDto.getUrl());
        String encryptedDescription = encrypt(passwordDto.getDescription());

        return new PasswordDto(encryptedUsername, encryptedPassword, encryptedUrl, encryptedDescription, passwordDto.getUserId());
    }

    public PasswordDto decryptPassword(PasswordDto passwordDto) throws Exception {
        String decryptedPassword = decrypt(passwordDto.getPassword());
        String decryptedUsername = decrypt(passwordDto.getUsername());
        String decryptedUrl = decrypt(passwordDto.getUrl());
        String decryptedDescription = decrypt(passwordDto.getDescription());
        return new PasswordDto(decryptedUsername, decryptedPassword, decryptedUrl, decryptedDescription, passwordDto.getUserId());
    }


}
