package com.example.purpledog.vo.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder @AllArgsConstructor
public class ReqUpdateUser {
    private String id;

    @NotNull(message = "password cannot be null")
    @Size(min = 6, message = "password not be less than 6 characters")
    private String password;
}
