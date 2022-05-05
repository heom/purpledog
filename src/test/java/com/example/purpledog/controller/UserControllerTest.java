package com.example.purpledog.controller;

import com.example.purpledog.dto.UserDto;
import com.example.purpledog.exception.common.ExceptionCode;
import com.example.purpledog.repository.UserRepository;
import com.example.purpledog.security.JwtAuthenticationUtils;
import com.example.purpledog.service.UserService;
import com.example.purpledog.vo.request.ReqCreateUser;
import com.example.purpledog.vo.request.ReqLoginUser;
import com.example.purpledog.vo.request.ReqUpdateUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtAuthenticationUtils jwtAuthenticationUtils;

    @AfterEach
    public void afterEach(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("createUser")
    public void createUser() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        ReqCreateUser reqCreateUser = ReqCreateUser.builder()
                                                .id(id)
                                                .password(rawPwd)
                                                .build();

        // when & then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqCreateUser)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(id));
    }

    @Test
    @DisplayName("createUser_parameter_error")
    public void createUser_parameter_error() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "te";
        ReqCreateUser reqCreateUser = ReqCreateUser.builder()
                                                .id(id)
                                                .password(rawPwd)
                                                .build();

        // when & then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqCreateUser)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value(ExceptionCode.BAD_PARAMETER_ERROR.getError()));
    }

    @Test
    @DisplayName("createUser_overlap")
    public void createUser_overlap() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);
        ReqCreateUser reqCreateUser = ReqCreateUser.builder()
                                                    .id(id)
                                                    .password(rawPwd)
                                                    .build();

        // when & then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqCreateUser)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(ExceptionCode.ACCOUNT_EXISTS_ERROR.getResultMsg()));
    }

    @Test
    @DisplayName("login")
    public void login() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);
        ReqLoginUser reqLoginUser = ReqLoginUser.builder()
                                                    .id(id)
                                                    .password(rawPwd)
                                                    .build();

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqLoginUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("access_token").exists());
    }

    @Test
    @DisplayName("login_parameter_error")
    public void login_parameter_error() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);
        ReqLoginUser reqLoginUser = ReqLoginUser.builder()
                                                    .id(id)
                                                    .build();

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqLoginUser)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value(ExceptionCode.BAD_PARAMETER_ERROR.getError()));
    }

    @Test
    @DisplayName("login_not_found")
    public void login_not_found() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        ReqLoginUser reqCreateUser = ReqLoginUser.builder()
                                                    .id(id)
                                                    .password(rawPwd)
                                                    .build();

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqCreateUser)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(ExceptionCode.ACCOUNT_NOT_EXISTS.getResultMsg()));
    }

    @Test
    @DisplayName("login_password_error")
    public void login_password_error() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);
        ReqLoginUser reqLoginUser = ReqLoginUser.builder()
                                                    .id(id)
                                                    .password(rawPwd+1)
                                                    .build();

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqLoginUser)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(ExceptionCode.ACCOUNT_PASSWORD_ERROR.getResultMsg()));
    }

    @Test
    @DisplayName("updateUser")
    public void updateUser() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);

        String jwtToken = jwtAuthenticationUtils.createToken(userDto);

        ReqUpdateUser reqUpdateUser = ReqUpdateUser.builder()
                                                    .password(rawPwd)
                                                    .build();

        // when & then
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .content(objectMapper.writeValueAsString(reqUpdateUser)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id));
    }

    @Test
    @DisplayName("updateUser_token_error")
    public void updateUser_token_error() throws Exception {
        // given
        String rawPwd = "testPassword";

        String jwtToken = "";

        ReqUpdateUser reqUpdateUser = ReqUpdateUser.builder()
                                                    .password(rawPwd)
                                                    .build();

        // when & then
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .content(objectMapper.writeValueAsString(reqUpdateUser)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(ExceptionCode.JWT_COMMON_ERROR.getResultMsg()));
    }

    @Test
    @DisplayName("updateUser_parameter_error")
    public void updateUser_parameter_error() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);

        String jwtToken = jwtAuthenticationUtils.createToken(userDto);

        ReqUpdateUser reqUpdateUser = ReqUpdateUser.builder()
                                                    .build();

        // when & then
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .content(objectMapper.writeValueAsString(reqUpdateUser)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value(ExceptionCode.BAD_PARAMETER_ERROR.getError()));
    }

    @Test
    @DisplayName("updateUser_not_found")
    public void updateUser_not_found() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();

        String jwtToken = jwtAuthenticationUtils.createToken(userDto);

        ReqUpdateUser reqUpdateUser = ReqUpdateUser.builder()
                                                    .password(rawPwd)
                                                    .build();

        // when & then
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .content(objectMapper.writeValueAsString(reqUpdateUser)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(ExceptionCode.JWT_INVALID_ACCOUNT.getResultMsg()));
    }

    @Test
    @DisplayName("deleteUser")
    public void deleteUser() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);

        String jwtToken = jwtAuthenticationUtils.createToken(userDto);

        // when & then
        mockMvc.perform(delete("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id));
    }

    @Test
    @DisplayName("deleteUser_token_error")
    public void deleteUser_token_error() throws Exception {
        // given
        String jwtToken = "";

        // when & then
        mockMvc.perform(delete("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtToken))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(ExceptionCode.JWT_COMMON_ERROR.getResultMsg()));
    }

    @Test
    @DisplayName("deleteUser_not_found")
    public void deleteUser_not_found() throws Exception {
        // given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();

        String jwtToken = jwtAuthenticationUtils.createToken(userDto);

        // when & then
        mockMvc.perform(delete("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, jwtToken))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("message").value(ExceptionCode.JWT_INVALID_ACCOUNT.getResultMsg()));
    }
}