package com.unipi.msc.riseupapi.Response;

import com.unipi.msc.riseupapi.Model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        if (u == null) return null;
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
    public static List<UserPresenter> getPresenter(Collection<User> users) {
        List<UserPresenter> presenters = new ArrayList<>();
        users.forEach(u -> presenters.add(getPresenter(u)));
        return presenters;
    }
    public static List<UserPresenter> getPresenter(List<User> users){
        if (users == null) return new ArrayList<>();
        List<UserPresenter> presenters = new ArrayList<>();
        users.forEach(u -> presenters.add(getPresenter(u)));
        return presenters;
    }

    public static UserPresenter getPresenter(User u, String token){
        UserPresenter userPresenter = getPresenter(u);
        userPresenter.setToken(token);
        return userPresenter;
    }
}
