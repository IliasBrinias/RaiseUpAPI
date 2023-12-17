package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Model.Task;
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
    private Long dueTo;
    private boolean completed;
    private List<UserPresenter> users = new ArrayList<>();
    private List<CommentPresenter> comments = new ArrayList<>();
    public static TaskPresenter getPresenter(Task task){
        return TaskPresenter.builder()
                .id(task.getId())
                .title(task.getTitle())
                .dueTo(task.getDueTo())
                .completed(task.isCompleted())
                .users(UserPresenter.getPresenter(task.getUsers()))
                .comments(CommentPresenter.getPresenter(task.getComments()))
                .build();
    }
    public static List<TaskPresenter> getPresenter(List<Task> tasks) {
        List<TaskPresenter> presenters = new ArrayList<>();
        tasks.forEach(task -> presenters.add(TaskPresenter.getPresenter(task)));
        return presenters;
    }
}
