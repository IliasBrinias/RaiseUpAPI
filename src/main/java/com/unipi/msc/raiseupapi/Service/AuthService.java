package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Config.JwtService;
import com.unipi.msc.raiseupapi.Interface.IAuth;
import com.unipi.msc.raiseupapi.Model.Role;
import com.unipi.msc.raiseupapi.Model.User.User;
import com.unipi.msc.raiseupapi.Model.User.UserRepository;
import com.unipi.msc.raiseupapi.Request.LoginRequest;
import com.unipi.msc.raiseupapi.Request.RegisterRequest;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthService implements IAuth {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Override
    public ResponseEntity<?> register(RegisterRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .build();
        user = userRepository.save(user);
        return GenericResponse.builder().data(LoginResponse.getResponse(user,generateToken(user))).build().success();
    }

    @Override
    public ResponseEntity<?> login(LoginRequest request) {
        User user;
        if (request.getUsername()!=null){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
            user = userRepository.findByUsername(request.getUsername()).orElse(null);
        }else {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
            user = userRepository.findByEmail(request.getEmail()).orElse(null);
        }
        if (user == null) return  ResponseEntity.notFound().build();
        return GenericResponse.builder().data(LoginResponse.getResponse(user,generateToken(user))).build().success();
    }
    public String generateToken(User user) {
        String generatedToken = jwtService.generateToken(new User(){
            @Override
            public String getUsername() {
                if (user.getUsername()!=null){
                    return user.getUsername();
                }else {
                    return user.getEmail();
                }
            }
        });
        return generatedToken;
    }
}
