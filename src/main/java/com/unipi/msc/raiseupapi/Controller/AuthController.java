package com.unipi.msc.raiseupapi.Controller;

import com.unipi.msc.raiseupapi.Interface.IAuth;
import com.unipi.msc.raiseupapi.Request.LoginRequest;
import com.unipi.msc.raiseupapi.Request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuth iAuth;

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        return iAuth.register(request);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        return iAuth.login(request);
    }
}
