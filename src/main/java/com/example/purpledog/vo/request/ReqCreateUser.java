package com.example.purpledog.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel
@Data
@Builder @AllArgsConstructor
public class ReqCreateUser {
    @ApiModelProperty(notes = "아이디", example = "testId", required = true)
    @NotNull(message = "id cannot be null")
    @Size(min = 4, message = "id not be less than 4 characters")
    private String id;

    @ApiModelProperty(notes = "비밀번호", example = "testPassword", required = true)
    @NotNull(message = "password cannot be null")
    @Size(min = 6, message = "password not be less than 6 characters")
    private String password;
}
