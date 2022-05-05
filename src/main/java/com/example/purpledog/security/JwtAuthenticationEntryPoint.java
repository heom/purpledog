package com.example.purpledog.security;


import com.example.purpledog.exception.common.ExceptionCode;
import com.example.purpledog.exception.common.ExceptionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private final ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		ExceptionCode exceptionCode = ExceptionCode.JWT_COMMON_ERROR;

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(exceptionCode.getStatus().value());

		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
																.timestamp(new Date())
																.status(exceptionCode.getStatus().value())
																.error(exceptionCode.getError())
																.message(exceptionCode.getResultMsg())
																.path(request.getRequestURI())
																.build();
		response.getOutputStream()
				.println(objectMapper.writeValueAsString(exceptionResponse));
	}
}
