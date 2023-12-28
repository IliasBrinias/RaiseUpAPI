package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Model.Step;
import com.unipi.msc.raiseupapi.Model.Task;
import com.unipi.msc.raiseupapi.Repository.StepRepository;
import jakarta.persistence.Column;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPresenter {
    private Long id;
    private String title;
    private String description;
    private Long dueTo;
    private boolean completed;
    private StepPresenter step;
    private List<UserPresenter> users = new ArrayList<>();
    private List<CommentPresenter> comments = new ArrayList<>();
    private List<TagPresenter> tags = new ArrayList<>();
    public static TaskPresenter getPresenter(Task task){
        return TaskPresenter.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueTo(task.getDueTo())
                .completed(task.isCompleted())
                .users(UserPresenter.getPresenter(task.getUsers()))
                .comments(CommentPresenter.getPresenter(task.getComments()))
                .tags(TagPresenter.getPresenter(task.getTags()))
                .step(StepPresenter.getPresenterWithoutTask(task.getStep()))
                .build();
    }
    public static List<TaskPresenter> getPresenter(List<Task> tasks) {
        if (tasks == null) return null;
        List<TaskPresenter> presenters = new ArrayList<>();
        tasks.forEach(task -> presenters.add(TaskPresenter.getPresenter(task)));
        return presenters;
    }
}
