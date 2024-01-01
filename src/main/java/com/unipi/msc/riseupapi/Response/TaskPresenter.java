package com.unipi.msc.riseupapi.Response;

import com.unipi.msc.riseupapi.Model.Task;
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
    private ColumnPresenter step;
    private List<UserPresenter> users = new ArrayList<>();
    private List<TagPresenter> tags = new ArrayList<>();
    public static TaskPresenter getPresenter(Task task){
        return TaskPresenter.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueTo(task.getDueTo())
                .completed(task.isCompleted())
                .users(UserPresenter.getPresenter(task.getUsers()))
                .tags(TagPresenter.getPresenter(task.getTags()))
                .step(ColumnPresenter.getPresenterWithoutTask(task.getStep()))
                .build();
    }
    public static List<TaskPresenter> getPresenter(List<Task> tasks) {
        if (tasks == null) return null;
        List<TaskPresenter> presenters = new ArrayList<>();
        tasks.forEach(task -> presenters.add(TaskPresenter.getPresenter(task)));
        return presenters;
    }
}
