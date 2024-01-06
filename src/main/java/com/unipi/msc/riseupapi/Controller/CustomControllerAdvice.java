package com.unipi.msc.riseupapi.Controller;

import com.unipi.msc.riseupapi.Response.GenericResponse;
import com.unipi.msc.riseupapi.Shared.ErrorMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<?> handleConflict(RuntimeException ex, WebRequest request) {
        if (ex instanceof InternalAuthenticationServiceException || ex instanceof InsufficientAuthenticationException){
            return GenericResponse.builder().message(ErrorMessages.ACCESS_DENIED).build().unauthorized();
        }else if (ex instanceof BadCredentialsException) {
            return GenericResponse.builder().message(ErrorMessages.BAD_CREDENTIALS).build().badRequest();
        }else {
            return GenericResponse.builder().build().internalServerError();
        }
    }
}