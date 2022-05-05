package com.example.purpledog.vo.response;

import com.example.purpledog.entity.UserEntity;
import lombok.Data;

@Data
public class ResCreateUser {
    private String id;

    public ResCreateUser(UserEntity userEntity){
        this.id = userEntity.getId();
    }
}
