package com.unipi.msc.raiseupapi.Config;

import com.google.gson.Gson;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Shared.ErrorMessages;
import com.unipi.msc.raiseupapi.Shared.Tags;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Configuration
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.reset();
        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(Tags.APPLICATION_JSON);
        ResponseEntity<?> responseEntity = GenericResponse.builder().message(ErrorMessages.ACCESS_DENIED).build().accessDenied();
        response.getOutputStream().print(new Gson().toJson(responseEntity));
    }
}
