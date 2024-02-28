package ru.vsu.cs.artfolio.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.vsu.cs.artfolio.dto.RestExceptionDto;

import java.io.IOException;


@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String JWT_EXPIRED = "ERROR";
    public static final String RUNTIME_ERROR = "RUNTIME";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String errorMessage = "Unauthorized path";
        int statusCode = HttpServletResponse.SC_UNAUTHORIZED;

        if (request.getAttribute(RUNTIME_ERROR) != null) {
            errorMessage = request.getAttribute(RUNTIME_ERROR).toString();
            statusCode = HttpServletResponse.SC_BAD_GATEWAY;
        } else if (request.getAttribute(JWT_EXPIRED) != null) {
            errorMessage = request.getAttribute(JWT_EXPIRED).toString();
        }

        response.setStatus(statusCode);
        OBJECT_MAPPER.writeValue(response.getOutputStream(), new RestExceptionDto(errorMessage));
    }

}
