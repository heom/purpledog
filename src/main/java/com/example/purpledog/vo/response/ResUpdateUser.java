package com.example.purpledog.vo.response;

import com.example.purpledog.entity.UserEntity;
import lombok.Data;

@Data
public class ResUpdateUser {
    private String id;

    public ResUpdateUser(UserEntity userEntity){
        this.id = userEntity.getId();
    }
}
