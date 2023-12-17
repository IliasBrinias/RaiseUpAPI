package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Model.Step;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StepPresenter {
    private Long id;
    private String title;
    private List<TaskPresenter> tasks = new ArrayList<>();
    public static List<StepPresenter> getPresenter(List<Step> steps) {
        List<StepPresenter> presenters = new ArrayList<>();
        steps.forEach(step -> presenters.add(getPresenter(step)));
        return presenters;
    }
    public static StepPresenter getPresenter(Step step){
        return StepPresenter.builder()
                .id(step.getId())
                .title(step.getTitle())
                .tasks(TaskPresenter.getPresenter(step.getTasks()))
                .build();
    }
}
