package com.example.purpledog.vo.response;

import com.example.purpledog.dto.UserDto;
import lombok.Data;

@Data
public class ResDeleteUser {
    private String id;

    public ResDeleteUser(UserDto userDto){
        this.id = userDto.getId();
    }
}
