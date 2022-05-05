package com.example.purpledog.service;

import com.example.purpledog.dto.UserDto;
import com.example.purpledog.entity.UserEntity;
import com.example.purpledog.exception.AccountException;
import com.example.purpledog.exception.ServerException;
import com.example.purpledog.exception.common.ExceptionCode;
import com.example.purpledog.repository.UserRepository;
import com.example.purpledog.security.JwtAuthenticationUtils;
import com.example.purpledog.vo.response.ResCreateUser;
import com.example.purpledog.vo.response.ResDeleteUser;
import com.example.purpledog.vo.response.ResLoginUser;
import com.example.purpledog.vo.response.ResUpdateUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService  {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationUtils jwtAuthenticationUtils;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id){
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);

        if(!optionalUserEntity.isPresent())
            throw new UsernameNotFoundException(id);

        UserEntity userEntity = optionalUserEntity.get();

        return new User(userEntity.getId(), userEntity.getPassword()
                , true, true, true, true
                , new ArrayList<>());
    }

    private void authenticateUserPwd(UserDto userDto){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getId(), userDto.getPassword()));
        }catch(BadCredentialsException e) {
            throw new AccountException(ExceptionCode.ACCOUNT_PASSWORD_ERROR);
        }catch(Exception e) {
            throw new ServerException(ExceptionCode.SERVER_COMMON_ERROR);
        }
    }

    @Override
    public ResCreateUser createUser(UserDto userDto) {
        if(userRepository.existsById(userDto.getId()))
            throw new AccountException(ExceptionCode.ACCOUNT_EXISTS_ERROR);

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity userEntity = userRepository.save(new UserEntity(userDto));

        return new ResCreateUser(userEntity);
    }

    @Override
    public ResLoginUser login(UserDto userDto) {
        if(!userRepository.existsById(userDto.getId()))
            throw new AccountException(ExceptionCode.ACCOUNT_NOT_EXISTS);

        authenticateUserPwd(userDto);

        try{
            userDto.setAccess_token(jwtAuthenticationUtils.createToken(userDto));
        }catch (Exception e){
            throw new ServerException(ExceptionCode.SERVER_TOKEN_CREATE_ERROR);
        }

        return new ResLoginUser(userDto);
    }

    @Transactional
    @Override
    public ResUpdateUser updateUser(UserDto userDto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userDto.getId());

        if(!optionalUserEntity.isPresent())
            throw new AccountException(ExceptionCode.ACCOUNT_NOT_EXISTS);

        UserEntity userEntity = optionalUserEntity.get();
        userEntity.updatePassword(passwordEncoder.encode(userDto.getPassword()));

        UserEntity newUserEntity = userRepository.save(userEntity);

        return new ResUpdateUser(newUserEntity);
    }

    @Transactional
    @Override
    public ResDeleteUser deleteUser(UserDto userDto) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userDto.getId());

        if(!optionalUserEntity.isPresent())
            throw new AccountException(ExceptionCode.ACCOUNT_NOT_EXISTS);

        UserEntity userEntity = optionalUserEntity.get();

        userRepository.delete(userEntity);

        return new ResDeleteUser(userDto);
    }
}
