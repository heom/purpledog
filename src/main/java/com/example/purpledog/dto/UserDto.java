package com.example.purpledog.dto;

import com.example.purpledog.entity.UserEntity;
import com.example.purpledog.vo.request.ReqCreateUser;
import com.example.purpledog.vo.request.ReqLoginUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder @AllArgsConstructor
public class UserDto {
    private String id;
    private String password;
    private String access_token;

    public UserDto(UserEntity userEntity){
        this.id = userEntity.getId();
        this.password = userEntity.getPassword();
    }

    public UserDto(ReqCreateUser reqCreateUser){
        this.id = reqCreateUser.getId();
        this.password = reqCreateUser.getPassword();
    }

    public UserDto(ReqLoginUser reqLoginUser){
        this.id = reqLoginUser.getId();
        this.password = reqLoginUser.getPassword();
    }
}
