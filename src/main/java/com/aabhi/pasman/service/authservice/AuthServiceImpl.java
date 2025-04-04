package com.aabhi.pasman.service.authservice;

import com.aabhi.pasman.dto.user.LoginDto;
import com.aabhi.pasman.dto.user.LoginResponse;
import com.aabhi.pasman.dto.user.UserDto;
import com.aabhi.pasman.model.User;
import com.aabhi.pasman.repository.UserRepository;
import com.aabhi.pasman.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );
        User authenticatedUser = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(authenticatedUser);
        String token = jwtService.generateToken(authenticatedUser);

        System.out.println(token);
        return LoginResponse.builder()
                .token(token)
                .encryptionSalt(authenticatedUser.getEncryptionSalt())
                .build();
    }

    @Override
    public String register(UserDto userDto) {
        if (userDto.getUsername() == null || userDto.getEmail() == null || userDto.getPassword() == null) {
            return "null";
        }

        String username = userDto.getUsername();
        String email = userDto.getEmail();
        String rawPassword = userDto.getPassword();

        String password = bCryptPasswordEncoder.encode(rawPassword);

        // Email validation
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        if (!email.matches(emailRegex)) {
            return "null";
        }

        // Username validation (alphanumeric)
        String usernameRegex = "^[a-zA-Z0-9]*$";
        if (!username.matches(usernameRegex)) {
            return "null";
        }

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        // 🧂 Generate AES encryption salt
        String encryptionSalt = AESKeyUtils.generateSalt(); // Must be Base64-encoded

        // 🔐 Build and save the user
        User user = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .encryptionSalt(encryptionSalt)
                .build();

        userRepository.save(user);

        // ✅ Return JWT token
        return jwtService.generateToken(user);
    }


    @Override
    public boolean checkUser(String id){
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    @Override
    public User getUserById(String id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }
}

class AESKeyUtils {
    public static String generateSalt() {
        byte[] salt = new byte[16]; // 128-bit salt
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}

