package com.example.purpledog.service;

import com.example.purpledog.dto.UserDto;
import com.example.purpledog.vo.response.ResCreateUser;
import com.example.purpledog.vo.response.ResDeleteUser;
import com.example.purpledog.vo.response.ResLoginUser;
import com.example.purpledog.vo.response.ResUpdateUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    ResCreateUser createUser(UserDto userDto);
    ResLoginUser login(UserDto userDto);
    ResUpdateUser updateUser(UserDto userDto);
    ResDeleteUser deleteUser(UserDto userDto);
}
