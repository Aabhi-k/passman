package com.aabhi.pasman.service.authservice;

import com.aabhi.pasman.dto.user.LoginDto;
import com.aabhi.pasman.dto.user.UserDto;
import com.aabhi.pasman.model.User;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(UserDto userDto);

    boolean checkUser(Long id);
    User getUserById(Long id);

}
