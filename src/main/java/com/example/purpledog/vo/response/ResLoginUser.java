package com.example.purpledog.vo.response;

import com.example.purpledog.dto.UserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ResLoginUser {
    @ApiModelProperty(notes = "아이디")
    private String id;
    @ApiModelProperty(notes = "JWT 토큰")
    private String access_token;

    public ResLoginUser(UserDto userDto){
        this.id = userDto.getId();
        this.access_token = userDto.getAccess_token();
    }
}
