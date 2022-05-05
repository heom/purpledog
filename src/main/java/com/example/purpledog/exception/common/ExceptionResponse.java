package com.example.purpledog.exception.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@ApiModel
@Data
@AllArgsConstructor
@Builder @NoArgsConstructor
public class ExceptionResponse {
    @ApiModelProperty(notes = "에러 발생 시간")
    private Date timestamp;
    @ApiModelProperty(notes = "에러 HttpStatus 코드")
    private Integer status;
    @ApiModelProperty(notes = "에러 대분류")
    private String error;
    @ApiModelProperty(notes = "에러 상세 분류")
    private String message;
    @ApiModelProperty(notes = "에러 발생 API URL")
    private String path;
}
