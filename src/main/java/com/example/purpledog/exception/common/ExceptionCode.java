package com.example.purpledog.exception.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode{

	ACCOUNT_EXISTS_ERROR(HttpStatus.UNAUTHORIZED, "Account Error","Account already exists"),
	ACCOUNT_PASSWORD_ERROR(HttpStatus.UNAUTHORIZED, "Account Error", "Incorrect password"),
	ACCOUNT_NOT_EXISTS(HttpStatus.UNAUTHORIZED,"Account Error",  "Account not exists"),

	JWT_COMMON_ERROR(HttpStatus.UNAUTHORIZED, "JWT Error", "JWT unauthorized"),
	JWT_INVALID_ERROR(HttpStatus.UNAUTHORIZED, "JWT Error", "Unavailable JWT token"),
	JWT_EXP_ERROR(HttpStatus.UNAUTHORIZED, "JWT Error", "Expired JWT token"),
	JWT_INVALID_ACCOUNT(HttpStatus.UNAUTHORIZED, "JWT Error", "Unavailable JWT token by account"),

	BAD_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "Bad Request", "Wrong parameter"),

	SERVER_COMMON_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error", "Server error"),
	SERVER_TOKEN_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error", "Create JWT token error"),
	SERVER_DB_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error", "Database error");

	private HttpStatus status;
	private String error;
	private String resultMsg;

	ExceptionCode(HttpStatus status, String error, String resultMsg) {
		this.status = status;
		this.error = error;
		this.resultMsg = resultMsg;
	}
}
