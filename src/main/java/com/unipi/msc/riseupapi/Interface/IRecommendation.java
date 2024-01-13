package com.unipi.msc.riseupapi.Interface;

import com.unipi.msc.riseupapi.Model.Task;
import com.unipi.msc.riseupapi.Model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IRecommendation {
    List<User> recommendUsers(Task task);
}
