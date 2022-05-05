package com.example.purpledog.exception.common;

import com.example.purpledog.exception.AccountException;
import com.example.purpledog.exception.ServerException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;


@RestController
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccountException.class)
    public final ResponseEntity<Object> handleAccountException(AccountException ex, WebRequest request){
        ExceptionResponse exceptionResponse = getDefaultExceptionResponse(ex.getExceptionCode(), request);
        return new ResponseEntity(exceptionResponse, ex.getExceptionCode().getStatus());
    }

    @ExceptionHandler(ServerException.class)
    public final ResponseEntity<Object> handleServerException(ServerException ex, WebRequest request){
        ExceptionResponse exceptionResponse = getDefaultExceptionResponse(ex.getExceptionCode(), request);
        return new ResponseEntity(exceptionResponse, ex.getExceptionCode().getStatus());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ExceptionResponse> handleDataAccessException(DataAccessException ex, WebRequest request) {
        ex.printStackTrace();
        ExceptionCode exceptionCode = ExceptionCode.SERVER_DB_ERROR;
        ExceptionResponse exceptionResponse = getDefaultExceptionResponse(exceptionCode, request);
        return new ResponseEntity(exceptionResponse, exceptionCode.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex
                                                                , HttpHeaders headers
                                                                , HttpStatus status
                                                                , WebRequest request) {
        ExceptionCode exceptionCode = ExceptionCode.BAD_PARAMETER_ERROR;
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date()
                                                                    , exceptionCode.getStatus().value()
                                                                    , exceptionCode.getError()
                                                                    , ex.getBindingResult().toString()
                                                                    , ((ServletWebRequest)request).getRequest().getRequestURI());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        ex.printStackTrace();
        ExceptionCode exceptionCode = ExceptionCode.SERVER_COMMON_ERROR;
        ExceptionResponse exceptionResponse = getDefaultExceptionResponse(exceptionCode, request);
        return new ResponseEntity(exceptionResponse, exceptionCode.getStatus());
    }

    private ExceptionResponse getDefaultExceptionResponse(ExceptionCode exceptionCode, WebRequest request) {
        return new ExceptionResponse(new Date()
                                    , exceptionCode.getStatus().value()
                                    , exceptionCode.getError()
                                    , exceptionCode.getResultMsg()
                                    , ((ServletWebRequest)request).getRequest().getRequestURI());
    }
}
