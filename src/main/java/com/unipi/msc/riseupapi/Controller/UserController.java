package com.unipi.msc.riseupapi.Controller;

import com.unipi.msc.riseupapi.Interface.IUser;
import com.unipi.msc.riseupapi.Request.EditUserRequest;
import com.unipi.msc.riseupapi.Request.FCMRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final IUser iUser;
    @GetMapping
    public ResponseEntity<?> getUser(){
        return iUser.getUser();
    }
    @GetMapping("all")
    public ResponseEntity<?> getUsers(){
        return iUser.getUsers();
    }
    @GetMapping("image/{userId}")
    public ResponseEntity<?> getUserImage(@PathVariable Long userId){
        return iUser.getUserImage(userId);
    }
    @GetMapping("search")
    public ResponseEntity<?> searchUser(@RequestParam Long boardId, @RequestParam boolean allUsers, @RequestParam String keyword){
        return iUser.searchUser(boardId, allUsers,keyword);
    }
    @PatchMapping
    public ResponseEntity<?> editUser(@ModelAttribute EditUserRequest request){
        return iUser.editUser(request);
    }
    @PatchMapping("fcm")
    public ResponseEntity<?> editUserFCM(@RequestBody FCMRequest request){
        return iUser.updateUserFCM(request);
    }
}
