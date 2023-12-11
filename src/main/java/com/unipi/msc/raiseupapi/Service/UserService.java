package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Interface.IUser;
import com.unipi.msc.raiseupapi.Model.User;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Response.UserPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService implements IUser {
    @Override
    public ResponseEntity<?> getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication();
        return GenericResponse.builder().data(UserPresenter.getPresenter(user)).build().success();
    }
}
