package com.unipi.msc.riseupapi.Response;

import com.unipi.msc.riseupapi.Model.Step;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColumnPresenter {
    private Long id;
    private String title;
    private Long boardId;
    private List<TaskPresenter> tasks = new ArrayList<>();
    public static List<ColumnPresenter> getPresenter(List<Step> steps) {
        List<ColumnPresenter> presenters = new ArrayList<>();
        steps.forEach(step -> presenters.add(getPresenter(step)));
        return presenters;
    }
    public static ColumnPresenter getPresenter(Step step){
        return ColumnPresenter.builder()
                .id(step.getId())
                .title(step.getTitle())
                .boardId(step.getBoard().getId())
                .tasks(TaskPresenter.getPresenter(step.getTasks()))
                .build();
    }
    public static ColumnPresenter getPresenterWithoutTask(Step step){
        return ColumnPresenter.builder()
                .id(step.getId())
                .boardId(step.getBoard().getId())
                .title(step.getTitle())
                .build();
    }

}
