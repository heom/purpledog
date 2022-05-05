package com.example.purpledog.exception;

import com.example.purpledog.exception.common.ExceptionCode;
import lombok.Data;

@Data
public class ServerException extends RuntimeException {
    private ExceptionCode exceptionCode;

    public ServerException(ExceptionCode exceptionCode){
        super(exceptionCode.getResultMsg());
        this.exceptionCode = exceptionCode;
    }
}
