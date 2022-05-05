package com.example.purpledog.security;

import com.example.purpledog.dto.UserDto;
import com.example.purpledog.entity.UserEntity;
import com.example.purpledog.exception.common.ExceptionCode;
import com.example.purpledog.exception.common.ExceptionResponse;
import com.example.purpledog.repository.UserRepository;
import com.example.purpledog.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter {

    private final JwtAuthenticationUtils jwtAuthenticationUtils;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(CommonUtils.isTextNull(authorizationHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authorizationHeader.replace("Bearer", "");
        String id = null;

        try{
            id = jwtAuthenticationUtils.getIdByJwtToken(jwtToken);
        }catch (ExpiredJwtException e){
            setResponse(request, response, ExceptionCode.JWT_EXP_ERROR);
            return;
        }catch (Exception e) {
            setResponse(request, response, ExceptionCode.JWT_INVALID_ERROR);
            return;
        }

        if(!CommonUtils.isTextNull(id)) {
            Optional<UserEntity> optionalUserEntity = userRepository.findById(id);

            if(!optionalUserEntity.isPresent()){
                setResponse(request, response, ExceptionCode.JWT_INVALID_ACCOUNT);
                return;
            }
            UserEntity userEntity = optionalUserEntity.get();
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    new UserDto(userEntity),
                    null,
                    null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private void setResponse(HttpServletRequest request
                                            , HttpServletResponse response
                                            , ExceptionCode exceptionCode) throws IOException {
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
