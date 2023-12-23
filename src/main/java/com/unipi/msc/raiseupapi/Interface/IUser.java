package com.unipi.msc.raiseupapi.Interface;

import com.unipi.msc.raiseupapi.Request.EditUserRequest;
import org.springframework.http.ResponseEntity;

public interface IUser {
    ResponseEntity<?> getUser();

    ResponseEntity<?> editUser(EditUserRequest request);

    ResponseEntity<?> getUserImage(Long userId);

    ResponseEntity<?> searchUser(String keyword);

    ResponseEntity<?> getUsers();
}
