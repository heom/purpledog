package com.example.purpledog.vo.response;

import com.example.purpledog.dto.UserDto;
import lombok.Data;

@Data
public class ResLoginUser {
    private String id;
    private String access_token;

    public ResLoginUser(UserDto userDto){
        this.id = userDto.getId();
        this.access_token = userDto.getAccess_token();
    }
}
