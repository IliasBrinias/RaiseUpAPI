package com.unipi.msc.riseupapi.Interface;

import com.unipi.msc.riseupapi.Request.LoginRequest;
import com.unipi.msc.riseupapi.Request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface IAuth {
    ResponseEntity<?> register(RegisterRequest request);
    ResponseEntity<?> login(LoginRequest request);

    ResponseEntity<?> createAdmin(Long id);
}
