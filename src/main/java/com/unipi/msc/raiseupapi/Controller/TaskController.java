package com.unipi.msc.raiseupapi.Controller;

import com.unipi.msc.raiseupapi.Interface.ITask;
import com.unipi.msc.raiseupapi.Request.TaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TaskController {
    private final ITask iTask;
    @PostMapping("column/{columnId}/add-task")
    public ResponseEntity<?> getBoardEmployees(@PathVariable Long columnId, @RequestBody TaskRequest request){
        return iTask.createTask(columnId,request);
    }
    @GetMapping("task/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long taskId){
        return iTask.getTask(taskId);
    }
    @PatchMapping("task/{taskId}/move-to-column/{columnId}")
    public ResponseEntity<?> moveTask(@PathVariable Long taskId, @PathVariable Long columnId){
        return iTask.moveTask(taskId, columnId);
    }
}
