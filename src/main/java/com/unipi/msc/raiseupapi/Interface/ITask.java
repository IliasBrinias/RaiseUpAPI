package com.unipi.msc.raiseupapi.Interface;

import com.unipi.msc.raiseupapi.Request.TaskRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITask {
    ResponseEntity<?> createTask(TaskRequest request);

    ResponseEntity<?> getTask(Long taskId);

    ResponseEntity<?> moveTask(Long taskId, Long columnId);

    ResponseEntity<?> updateEmployees(Long taskId, List<Long> employeeIds);

    ResponseEntity<?> getTasks();

    ResponseEntity<?> removeEmployee(Long taskId, Long userId);

    ResponseEntity<?> editTaskDueDate(Long taskId, Long dueDate);

    ResponseEntity<?> editTask(Long taskId, TaskRequest request);

    ResponseEntity<?> searchTask(String keyword);

    ResponseEntity<?> deleteTask(Long taskId);
}
