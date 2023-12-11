package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Model.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String role;
    private String token;
    public static LoginResponse getResponse(User user, String token){
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .token(token)
                .build();
    }
}
