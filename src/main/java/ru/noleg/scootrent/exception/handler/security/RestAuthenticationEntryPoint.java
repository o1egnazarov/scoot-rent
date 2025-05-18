package ru.noleg.scootrent.exception.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.noleg.scootrent.exception.handler.ErrorCode;
import ru.noleg.scootrent.exception.handler.ExceptionResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(this.objectMapper.writeValueAsString(
                new ExceptionResponse(
                        ErrorCode.UNAUTHORIZED,
                        "Authentication is required",
                        request.getRequestURI(),
                        LocalDateTime.now()))
        );
    }
}