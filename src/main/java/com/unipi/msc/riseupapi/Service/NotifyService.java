package com.unipi.msc.riseupapi.Service;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.unipi.msc.riseupapi.Interface.IBoard;
import com.unipi.msc.riseupapi.Interface.INotify;
import com.unipi.msc.riseupapi.Model.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class NotifyService implements INotify {
    @Override
    public void notifyUsers(User signedUser, List<User> users, String taskTitle) {
        List<Message> messages = new ArrayList<>();
        users.forEach(user -> {
            if (user.getFcmId() == null) return;
            if (Objects.equals(user.getId(), signedUser.getId())) return;
            messages.add(Message.builder()
                .setToken(user.getFcmId())
                .putData("taskTitle", taskTitle)
                .putData("addedBy", signedUser.getFullName())
                .build());
        });
        if (messages.isEmpty()) return;
        try {
            FirebaseMessaging.getInstance().sendAll(messages);
        } catch (FirebaseMessagingException ignore) {}
    }
}
