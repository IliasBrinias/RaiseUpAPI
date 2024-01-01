package com.unipi.msc.riseupapi.Service;

import com.unipi.msc.riseupapi.Config.JwtService;
import com.unipi.msc.riseupapi.Interface.IAuth;
import com.unipi.msc.riseupapi.Model.Role;
import com.unipi.msc.riseupapi.Model.User;
import com.unipi.msc.riseupapi.Repository.UserRepository;
import com.unipi.msc.riseupapi.Request.LoginRequest;
import com.unipi.msc.riseupapi.Request.RegisterRequest;
import com.unipi.msc.riseupapi.Response.GenericResponse;
import com.unipi.msc.riseupapi.Response.UserPresenter;
import com.unipi.msc.riseupapi.Shared.ErrorMessages;
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
        if (userRepository.existsUserByUsername(request.getUsername())){
            GenericResponse.builder().message(ErrorMessages.USERNAME_EXISTS).build().success();
        }
        if (userRepository.existsUserByEmail(request.getEmail())){
            GenericResponse.builder().message(ErrorMessages.EMAIL_EXISTS).build().success();
        }
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.EMPLOYEE)
                .build();
        user = userRepository.save(user);
        return GenericResponse.builder().data(UserPresenter.getPresenter(user,generateToken(user))).build().success();
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
        return GenericResponse.builder().data(UserPresenter.getPresenter(user,generateToken(user))).build().success();
    }

    @Override
    public ResponseEntity<?> createAdmin(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return GenericResponse.builder().message(ErrorMessages.USER_NOT_FOUND).build().badRequest();
        user.setRole(Role.ADMIN);
        User u = userRepository.save(user);
        return GenericResponse.builder().data(UserPresenter.getPresenter(u)).build().success();
    }

    public String generateToken(User user) {
        return jwtService.generateToken(new User(){
            @Override
            public String getUsername() {
                if (user.getUsername()!=null){
                    return user.getUsername();
                }else {
                    return user.getEmail();
                }
            }
        });
    }
}
