package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Interface.ITask;
import com.unipi.msc.raiseupapi.Model.*;
import com.unipi.msc.raiseupapi.Repository.StepRepository;
import com.unipi.msc.raiseupapi.Repository.TagRepository;
import com.unipi.msc.raiseupapi.Repository.TaskRepository;
import com.unipi.msc.raiseupapi.Repository.UserRepository;
import com.unipi.msc.raiseupapi.Request.TaskRequest;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Response.TaskPresenter;
import com.unipi.msc.raiseupapi.Shared.ErrorMessages;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskService implements ITask {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final StepRepository stepRepository;
    @Override
    public ResponseEntity<?> createTask(Long columnId, TaskRequest request) {
        List<User> users = userRepository.findUsersByIdIn(request.getEmployeeIds()).orElse(null);
        if (users == null){
            return GenericResponse.builder().message(ErrorMessages.USER_NOT_FOUND).build().badRequest();
        }
        List<Tag> tags = tagRepository.findByIdIn(request.getTagIds()).orElse(null);
        if (tags == null){
            return GenericResponse.builder().message(ErrorMessages.TAG_NOT_FOUND).build().badRequest();
        }
        Step step = stepRepository.findById(columnId).orElse(null);
        if (step == null){
            return GenericResponse.builder().message(ErrorMessages.TAG_NOT_FOUND).build().badRequest();
        }
        Task task = taskRepository.save(Task.builder()
            .title(request.getTitle())
            .dueTo(request.getDueTo())
            .difficulty(request.getDifficulty())
            .step(step)
            .tags(tags)
            .users(users)
            .build());
        return GenericResponse.builder().data(TaskPresenter.getPresenter(task)).build().success();
    }

    @Override
    public ResponseEntity<?> getTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null){
            return GenericResponse.builder().message(ErrorMessages.TASK_NOT_FOUND).build().badRequest();
        }
        return GenericResponse.builder().data(TaskPresenter.getPresenter(task)).build().success();
    }

    @Override
    public ResponseEntity<?> moveTask(Long taskId, Long columnId) {

        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null){
            return GenericResponse.builder().message(ErrorMessages.TASK_NOT_FOUND).build().badRequest();
        }

        Step step = stepRepository.findById(columnId).orElse(null);
        if (step == null){
            return GenericResponse.builder().message(ErrorMessages.TAG_NOT_FOUND).build().badRequest();
        }

        Step oldStepTask = task.getStep();
        oldStepTask.getTasks().remove(task);
        stepRepository.save(oldStepTask);

        step.getTasks().add(task);
        stepRepository.save(step);

        task.setStep(step);
        task = taskRepository.save(task);

        return GenericResponse.builder().data(TaskPresenter.getPresenter(task)).build().success();
    }
}
