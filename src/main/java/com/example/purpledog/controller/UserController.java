package com.example.purpledog.controller;

import com.example.purpledog.dto.UserDto;
import com.example.purpledog.exception.common.ExceptionResponse;
import com.example.purpledog.service.UserService;
import com.example.purpledog.vo.request.ReqCreateUser;
import com.example.purpledog.vo.request.ReqLoginUser;
import com.example.purpledog.vo.request.ReqUpdateUser;
import com.example.purpledog.vo.response.ResCreateUser;
import com.example.purpledog.vo.response.ResDeleteUser;
import com.example.purpledog.vo.response.ResLoginUser;
import com.example.purpledog.vo.response.ResUpdateUser;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    @ApiOperation(value = "회원가입", notes = "회원가입"
                    , produces = "application/json", response = ResCreateUser.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Wrong parameter"
                        , response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "Account already exists"
                        , response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "Server error \t\n " +
                                                "Database error"
                        , response = ExceptionResponse.class)})
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<ResCreateUser> createUser(@Valid @RequestBody ReqCreateUser requestUser){
        ResCreateUser resCreateUser = userService.createUser(new UserDto(requestUser));
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateUser);
    }

    @PostMapping("/login")
    @ApiOperation(value = "로그인", notes = "로그인"
                    , produces = "application/json", response = ResLoginUser.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Wrong parameter"
                        , response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "Account not exists \t\n" +
                                                "Incorrect password"
                        , response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "Server error \t\n " +
                                                "Database error \t\n" +
                                                "Create JWT token error"
                        , response = ExceptionResponse.class)})
    public ResponseEntity<ResLoginUser> login(@Valid @RequestBody ReqLoginUser reqLoginUser) {
        ResLoginUser resLoginUser = userService.login(new UserDto(reqLoginUser));
        return ResponseEntity.status(HttpStatus.OK).body(resLoginUser);
    }

    @PutMapping("/users")
    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호 변경"
                    , produces = "application/json", response = ResUpdateUser.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access_token", required = true
                                , dataType = "string", paramType = "header")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Wrong parameter"
                        , response = ExceptionResponse.class),
            @ApiResponse(code = 401, message = "Account not exists \t\n" +
                                                "JWT unauthorized \t\n" +
                                                "Unavailable JWT token \t\n" +
                                                "Expired JWT token \t\n" +
                                                "Unavailable JWT token by account"
                        , response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "Server error \t\n " +
                                                "Database error"
                        , response = ExceptionResponse.class)})
    public ResponseEntity<ResUpdateUser> updateUser(@ApiIgnore @AuthenticationPrincipal UserDto userDto
                                                    , @Valid @RequestBody ReqUpdateUser reqUpdateUser) {
        userDto.setPassword(reqUpdateUser.getPassword());
        ResUpdateUser resUpdateUser = userService.updateUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(resUpdateUser);
    }

    @DeleteMapping("/users")
    @ApiOperation(value = "회원탈퇴", notes = "회원탈퇴"
                    , produces = "application/json", response = ResDeleteUser.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "access_token", required = true
                                , dataType = "string", paramType = "header")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Account not exists \t\n" +
                                                "JWT unauthorized \t\n" +
                                                "Unavailable JWT token \t\n" +
                                                "Expired JWT token \t\n" +
                                                "Unavailable JWT token by account"
                        , response = ExceptionResponse.class),
            @ApiResponse(code = 500, message = "Server error \t\n " +
                                                "Database error"
                        , response = ExceptionResponse.class)})
    public ResponseEntity<ResDeleteUser> deleteUser(@ApiIgnore @AuthenticationPrincipal UserDto userDto) {
        ResDeleteUser resDeleteUser = userService.deleteUser(userDto);
        return ResponseEntity.status(HttpStatus.OK).body(resDeleteUser);
    }
}
