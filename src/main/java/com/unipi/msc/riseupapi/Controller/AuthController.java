package com.unipi.msc.riseupapi.Controller;

import com.unipi.msc.riseupapi.Interface.IAuth;
import com.unipi.msc.riseupapi.Request.LoginRequest;
import com.unipi.msc.riseupapi.Request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("createAdmin/{id}")
    public ResponseEntity<?> createAdmin(@PathVariable Long id){
        return iAuth.createAdmin(id);
    }
}
