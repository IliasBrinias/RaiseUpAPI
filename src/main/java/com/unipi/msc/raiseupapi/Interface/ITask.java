package com.unipi.msc.raiseupapi.Interface;

import com.unipi.msc.raiseupapi.Request.TaskRequest;
import org.springframework.http.ResponseEntity;

public interface ITask {
    ResponseEntity<?> createTask(Long columnId, TaskRequest request);

    ResponseEntity<?> getTask(Long taskId);

    ResponseEntity<?> moveTask(Long taskId, Long columnId);
}
