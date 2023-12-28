package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Interface.ITask;
import com.unipi.msc.raiseupapi.Model.*;
import com.unipi.msc.raiseupapi.Repository.StepRepository;
import com.unipi.msc.raiseupapi.Repository.TagRepository;
import com.unipi.msc.raiseupapi.Repository.TaskRepository;
import com.unipi.msc.raiseupapi.Repository.UserRepository;
import com.unipi.msc.raiseupapi.Request.TaskRequest;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Response.TagPresenter;
import com.unipi.msc.raiseupapi.Response.TaskPresenter;
import com.unipi.msc.raiseupapi.Shared.ErrorMessages;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Override
    public ResponseEntity<?> updateEmployees(Long taskId, List<Long> employeeIds) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null){
            return GenericResponse.builder().message(ErrorMessages.TASK_NOT_FOUND).build().badRequest();
        }
        List<User> users = userRepository.findUsersByIdIn(employeeIds).orElse(null);
        if (users == null){
            return GenericResponse.builder().message(ErrorMessages.USER_NOT_FOUND).build().badRequest();
        }
        task.setUsers(users);
        task = taskRepository.save(task);

        for (User user:users) {
            if (!user.getTasks().contains(task)){
                user.getTasks().add(task);
                userRepository.save(user);
            }
        }

        return GenericResponse.builder().data(TaskPresenter.getPresenter(task)).build().success();
    }

    @Override
    public ResponseEntity<?> getTasks() {
        List<User> users = new ArrayList<>();
        users.add((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        List<TaskPresenter> presenters = TaskPresenter.getPresenter(taskRepository.findAllByUsersIn(users));
        return GenericResponse.builder().data(presenters).build().success();
    }

    @Override
    public ResponseEntity<?> removeEmployee(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null){
            return GenericResponse.builder().message(ErrorMessages.TASK_NOT_FOUND).build().badRequest();
        }
        User user = userRepository.findAllByIdIs(userId).orElse(null);
        if (user == null){
            return GenericResponse.builder().message(ErrorMessages.USER_NOT_FOUND).build().badRequest();
        }

        if (user.getTasks().remove(task)){
            user = userRepository.save(user);
        }

        if (task.getUsers().remove(user)){
            task = taskRepository.save(task);
        }
        TaskPresenter presenter = TaskPresenter.getPresenter(task);
        return GenericResponse.builder().data(presenter).build().success();
    }

    @Override
    public ResponseEntity<?> editTaskDueDate(Long taskId, Long dueDate) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null){
            return GenericResponse.builder().message(ErrorMessages.TASK_NOT_FOUND).build().badRequest();
        }
        task.setDueTo(dueDate);
        task = taskRepository.save(task);
        TaskPresenter presenter = TaskPresenter.getPresenter(task);
        return GenericResponse.builder().data(presenter).build().success();
    }

    @Override
    public ResponseEntity<?> editTask(Long taskId, TaskRequest request) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null){
            return GenericResponse.builder().message(ErrorMessages.TASK_NOT_FOUND).build().badRequest();
        }
        if (request.getTagIds()!=null){
            List<Tag> tagList = tagRepository.findByIdIn(request.getTagIds()).orElse(null);
            if (tagList == null){
                return GenericResponse.builder().message(ErrorMessages.TAG_NOT_FOUND).build().badRequest();
            }
            for (int i = 0; i < tagList.size(); i++) {
                if (!tagList.get(i).getTasks().contains(task)) {
                    tagList.get(i).getTasks().add(task);
                    tagList.set(i, tagRepository.save(tagList.get(i)));
                }
            }
            task.setTags(tagList);
            task = taskRepository.save(task);
        }
        if (request.getEmployeeIds()!=null){
            List<User> users = userRepository.findUsersByIdIn(request.getEmployeeIds()).orElse(null);
            if (users == null){
                return GenericResponse.builder().message(ErrorMessages.USER_NOT_FOUND).build().badRequest();
            }
            task.setUsers(users);
            task = taskRepository.save(task);
            for (User user:users) {
                if (!user.getTasks().contains(task)){
                    user.getTasks().add(task);
                    userRepository.save(user);
                }
            }
        }
        if (request.getColumnId()!=null){
            Step step = stepRepository.findById(request.getColumnId()).orElse(null);
            if (step == null){
                return GenericResponse.builder().message(ErrorMessages.STEP_NOT_FOUND).build().badRequest();
            }
            Step currentStep = task.getStep();
            if (currentStep.getTasks().remove(task)){
                stepRepository.save(currentStep);
            }
            task.setStep(step);
            task = taskRepository.save(task);
        }
        if (request.getTitle()!=null || request.getDueTo()!=null || request.getCompleted()!=null || request.getDifficulty()!=null ||request.getDescription()!=null){
            if (request.getTitle()!=null) task.setTitle(request.getTitle());
            if (request.getDescription()!=null) task.setDescription(request.getDescription());
            if (request.getDueTo()!=null) task.setDueTo(request.getDueTo());
            if (request.getCompleted()!=null) task.setCompleted(request.getCompleted());
            if (request.getDifficulty()!=null) task.setDifficulty(request.getDifficulty());
            task = taskRepository.save(task);
        }

        TaskPresenter presenter = TaskPresenter.getPresenter(task);
        return GenericResponse.builder().data(presenter).build().success();
    }

    @Override
    public ResponseEntity<?> searchTask(String keyword) {
        List<TagPresenter> presenter = TagPresenter.getPresenter(tagRepository.findAllByNameLike(keyword).orElse(null));
        return GenericResponse.builder().data(presenter).build().success();
    }
}
