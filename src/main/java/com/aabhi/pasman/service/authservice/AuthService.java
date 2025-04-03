package com.aabhi.pasman.service.authservice;

import com.aabhi.pasman.dto.user.LoginDto;
import com.aabhi.pasman.dto.user.UserDto;

public interface AuthService {
    String login(LoginDto loginDto);

    String register(UserDto userDto);

}
