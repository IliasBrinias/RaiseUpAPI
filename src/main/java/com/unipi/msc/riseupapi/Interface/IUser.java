package com.unipi.msc.riseupapi.Interface;

import com.unipi.msc.riseupapi.Request.EditUserRequest;
import com.unipi.msc.riseupapi.Request.FCMRequest;
import org.springframework.http.ResponseEntity;

public interface IUser {
    ResponseEntity<?> getUser();

    ResponseEntity<?> editUser(EditUserRequest request);

    ResponseEntity<?> getUserImage(Long userId);

    ResponseEntity<?> searchUser(Long boardId, boolean allUsers, String keyword);

    ResponseEntity<?> getUsers();

    ResponseEntity<?> updateUserFCM(FCMRequest request);
}
