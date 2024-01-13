package com.unipi.msc.riseupapi.Controller;

import com.unipi.msc.riseupapi.Interface.ITask;
import com.unipi.msc.riseupapi.Request.TaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("task")
@RequiredArgsConstructor
public class TaskController {
    private final ITask iTask;
    @GetMapping
    public ResponseEntity<?> getTasks(){
        return iTask.getTasks();
    }
    @GetMapping("search")
    public ResponseEntity<?> getTasks(@RequestParam String keyword){
        return iTask.searchTask(keyword);
    }
    @GetMapping("{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long taskId){
        return iTask.getTask(taskId);
    }
    @GetMapping("{taskId}/propose-users")
    public ResponseEntity<?> proposeUsers(@PathVariable Long taskId){
        return iTask.proposeUsers(taskId);
    }
    @PostMapping
    public ResponseEntity<?> getBoardEmployees(@RequestBody TaskRequest request){
        return iTask.createTask(request);
    }
    @PatchMapping("{taskId}/move-to-column/{columnId}")
    public ResponseEntity<?> moveTask(@PathVariable Long taskId, @PathVariable Long columnId){
        return iTask.moveTask(taskId, columnId);
    }
    @PatchMapping("{taskId}/employees")
    public ResponseEntity<?> moveTask(@PathVariable Long taskId, @RequestBody List<Long> employeeIds){
        return iTask.updateEmployees(taskId, employeeIds);
    }
    @PatchMapping("{taskId}/due-date")
    public ResponseEntity<?> editTaskDueDate(@PathVariable Long taskId, @RequestBody Long dueDate){
        return iTask.editTaskDueDate(taskId, dueDate);
    }
    @PatchMapping("{taskId}")
    public ResponseEntity<?> editTask(@PathVariable Long taskId, @RequestBody TaskRequest request){
        return iTask.editTask(taskId, request);
    }
    @DeleteMapping("{taskId}/delete-employee/{userId}")
    public ResponseEntity<?>deleteEmployee(@PathVariable Long taskId,@PathVariable Long userId){
        return iTask.removeEmployee(taskId, userId);
    }
    @DeleteMapping("{taskId}")
    public ResponseEntity<?>deleteTask(@PathVariable Long taskId){
        return iTask.deleteTask(taskId);
    }
}
