package com.unipi.msc.raiseupapi.Interface;

import com.unipi.msc.raiseupapi.Request.LoginRequest;
import com.unipi.msc.raiseupapi.Request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface IAuth {
    ResponseEntity<?> register(RegisterRequest request);
    ResponseEntity<?> login(LoginRequest request);
}
