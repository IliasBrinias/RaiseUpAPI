package com.unipi.msc.raiseupapi.Controller;

import com.unipi.msc.raiseupapi.Interface.ITask;
import com.unipi.msc.raiseupapi.Request.TaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("tasks")
    public ResponseEntity<?> getTasks(){
        return iTask.getTasks();
    }
    @GetMapping("tasks/search")
    public ResponseEntity<?> getTasks(@RequestParam String keyword){
        return iTask.searchTask(keyword);
    }
    @PatchMapping("task/{taskId}/move-to-column/{columnId}")
    public ResponseEntity<?> moveTask(@PathVariable Long taskId, @PathVariable Long columnId){
        return iTask.moveTask(taskId, columnId);
    }
    @PatchMapping("task/{taskId}/employees")
    public ResponseEntity<?> moveTask(@PathVariable Long taskId, @RequestBody List<Long> employeeIds){
        return iTask.updateEmployees(taskId, employeeIds);
    }
    @PatchMapping("task/{taskId}/due-date")
    public ResponseEntity<?> editTaskDueDate(@PathVariable Long taskId, @RequestBody Long dueDate){
        return iTask.editTaskDueDate(taskId, dueDate);
    }
    @PatchMapping("task/{taskId}")
    public ResponseEntity<?> editTask(@PathVariable Long taskId, @RequestBody TaskRequest request){
        return iTask.editTask(taskId, request);
    }
    @DeleteMapping("task/{taskId}/delete-employee/{userId}")
    public ResponseEntity<?>deleteEmployee(@PathVariable Long taskId,@PathVariable Long userId ){
        return iTask.removeEmployee(taskId, userId);
    }
}
