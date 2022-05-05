package com.example.purpledog.vo.response;

import com.example.purpledog.dto.UserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ResDeleteUser {
    @ApiModelProperty(notes = "아이디")
    private String id;

    public ResDeleteUser(UserDto userDto){
        this.id = userDto.getId();
    }
}
