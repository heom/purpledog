package com.example.purpledog.controller;

import com.example.purpledog.dto.UserDto;
import com.example.purpledog.service.UserService;
import com.example.purpledog.vo.request.ReqCreateUser;
import com.example.purpledog.vo.request.ReqLoginUser;
import com.example.purpledog.vo.request.ReqUpdateUser;
import com.example.purpledog.vo.response.ResCreateUser;
import com.example.purpledog.vo.response.ResDeleteUser;
import com.example.purpledog.vo.response.ResLoginUser;
import com.example.purpledog.vo.response.ResUpdateUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ResCreateUser> createUser(@Valid @RequestBody ReqCreateUser requestUser){
        ResCreateUser resCreateUser = userService.createUser(new UserDto(requestUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUser);
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginUser> login(@Valid @RequestBody ReqLoginUser reqLoginUser) {
        ResLoginUser resLoginUser = userService.login(new UserDto(reqLoginUser));
        return ResponseEntity.status(HttpStatus.OK).body(resLoginUser);
    }

    @PutMapping("/users")
    public ResponseEntity<ResUpdateUser> updateUser(@AuthenticationPrincipal UserDto userDto
                                                    , @Valid @RequestBody ReqUpdateUser reqUpdateUser) {
        userDto.setPassword(reqUpdateUser.getPassword());
        ResUpdateUser resUpdateUser = userService.updateUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(resUpdateUser);
    }

    @DeleteMapping("/users")
    public ResponseEntity<ResDeleteUser> deleteUser(@AuthenticationPrincipal UserDto userDto) {
        ResDeleteUser resDeleteUser = userService.deleteUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(resDeleteUser);
    }
}
