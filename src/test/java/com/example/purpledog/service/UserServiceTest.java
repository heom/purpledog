package com.example.purpledog.service;

import com.example.purpledog.dto.UserDto;
import com.example.purpledog.entity.UserEntity;
import com.example.purpledog.exception.AccountException;
import com.example.purpledog.repository.UserRepository;
import com.example.purpledog.vo.response.ResLoginUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager em;

    @AfterEach
    public void afterEach(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("createUser")
    public void createUser(){
        //given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);

        //when
        UserDetails userDetails = userService.loadUserByUsername(id);

        //then
        assertAll(
                () -> assertEquals(id, userDetails.getUsername()),
                () -> assertTrue(passwordEncoder.matches(rawPwd, userDetails.getPassword()))
        );
    }

    @Test
    @DisplayName("createUser_overlap")
    public void createUser_overlap(){
        //given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);

        //when & then
        assertThrows(AccountException.class, ()-> userService.createUser(userDto));
    }

    @Test
    @DisplayName("login")
    public void login(){
        //given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                    .id(id)
                                    .password(rawPwd)
                                    .build();
        userService.createUser(userDto);
        userDto = UserDto.builder()
                            .id(id)
                            .password(rawPwd)
                            .build();

        //when
        ResLoginUser resLoginUser = userService.login(userDto);

        //then
        assertAll(
                () -> assertEquals(id, resLoginUser.getId()),
                () -> assertNotNull(resLoginUser.getAccess_token())
        );
    }

    @Test
    @DisplayName("createUser_not_found")
    public void createUser_not_found(){
        //given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                .id(id)
                                .password(rawPwd)
                                .build();

        //when & then
        assertThrows(AccountException.class, ()-> userService.login(userDto));
    }

    @Test
    @DisplayName("createUser_password_error")
    public void createUser_password_error(){
        //given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                .id(id)
                                .password(rawPwd)
                                .build();
        userService.createUser(userDto);
        userDto.setPassword("123456");

        //when & then
        assertThrows(AccountException.class, ()-> userService.login(userDto));
    }

    @Test
    @DisplayName("updateUser")
    public void updateUser(){
        //given
        String id = "testId";
        String rawPwd = "testPassword";
        String newRawPwd = "123456";
        UserDto userDto = UserDto.builder()
                                .id(id)
                                .password(rawPwd)
                                .build();
        userService.createUser(userDto);
        userDto.setPassword(newRawPwd);

        //when
        userService.updateUser(userDto);
        em.flush();
        em.clear();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);

        //then
        assertAll(
                () -> assertTrue(optionalUserEntity.isPresent()),
                () -> assertEquals(id, optionalUserEntity.get().getId()),
                () -> assertTrue(passwordEncoder.matches(newRawPwd, optionalUserEntity.get().getPassword()))
        );
    }

    @Test
    @DisplayName("updateUser_not_found")
    public void updateUser_not_found(){
        //given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                .id(id)
                                .password(rawPwd)
                                .build();

        //when & then
        assertThrows(AccountException.class, ()-> userService.updateUser(userDto));
    }

    @Test
    @DisplayName("deleteUser")
    public void deleteUser(){
        //given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                .id(id)
                                .password(rawPwd)
                                .build();
        userService.createUser(userDto);

        //when
        userService.deleteUser(userDto);
        em.flush();
        em.clear();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);

        //then
        assertAll(
                () -> assertTrue(!optionalUserEntity.isPresent())
        );
    }

    @Test
    @DisplayName("deleteUser_not_found")
    public void deleteUser_not_found(){
        //given
        String id = "testId";
        String rawPwd = "testPassword";
        UserDto userDto = UserDto.builder()
                                .id(id)
                                .password(rawPwd)
                                .build();

        //when & then
        assertThrows(AccountException.class, ()-> userService.deleteUser(userDto));
    }
}