package com.aabhi.pasman.service.authservice;

import com.aabhi.pasman.dto.user.LoginDto;
import com.aabhi.pasman.dto.user.UserDto;
import com.aabhi.pasman.model.User;
import com.aabhi.pasman.repository.UserRepository;
import com.aabhi.pasman.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public String login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        User authenticatedUser = userRepository.findByEmail(loginDto.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        return jwtService.generateToken(authenticatedUser);
    }

    @Override
    public String register(UserDto userDto) {
        if(userDto.getUsername() == null || userDto.getEmail() == null || userDto.getPassword() == null){
            return "null";
        }

        String username = userDto.getUsername();
        String email = userDto.getEmail();
        String password = bCryptPasswordEncoder.encode(userDto.getPassword());


        // validate email
        String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        if(!email.matches(emailRegex)){
            return "null";
        }

        // Alphanumeric username
        String usernameRegex = "^[a-zA-Z0-9]*$";
        if(!username.matches(usernameRegex)){
            return "null";
        }

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()){
            throw new RuntimeException("User already exists");
        }

        User user = new User(username, email, password);
        userRepository.save(user);
        return jwtService.generateToken(user);
    }
}
