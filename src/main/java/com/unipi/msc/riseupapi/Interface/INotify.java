package com.unipi.msc.riseupapi.Interface;

import com.unipi.msc.riseupapi.Model.User;

import java.util.List;

public interface INotify {
    void notifyUsers(User signedUser, List<User> users, String taskTitle, Long taskId);
}
