package com.example.purpledog.vo.response;

import com.example.purpledog.entity.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel
@Data
public class ResCreateUser {
    @ApiModelProperty(notes = "아이디")
    private String id;

    public ResCreateUser(UserEntity userEntity){
        this.id = userEntity.getId();
    }
}
