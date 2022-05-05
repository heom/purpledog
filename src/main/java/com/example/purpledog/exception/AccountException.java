package com.example.purpledog.exception;

import com.example.purpledog.exception.common.ExceptionCode;
import lombok.Data;

@Data
public class AccountException extends RuntimeException {
    private ExceptionCode exceptionCode;

    public AccountException(ExceptionCode exceptionCode){
        super(exceptionCode.getResultMsg());
        this.exceptionCode = exceptionCode;
    }
}
