package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserPresenter {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String role;
    private String token;
    private String profile;

    public static UserPresenter getPresenter(User u){
        UserPresenter userPresenter = UserPresenter.builder()
                .id(u.getId())
                .email(u.getEmail())
                .username(u.getUsername())
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .role(u.getRole().name())
                .build();
        if (u.getImage()!=null){
            userPresenter.setProfile("user/image/"+u.getId());
        }
        return userPresenter;
    }
    public static UserPresenter getPresenter(User u, String token){
        UserPresenter userPresenter = getPresenter(u);
        userPresenter.setToken(token);
        return userPresenter;
    }
}
