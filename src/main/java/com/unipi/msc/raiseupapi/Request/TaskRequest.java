package com.unipi.msc.raiseupapi.Request;

import com.unipi.msc.raiseupapi.Model.Difficulty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class TaskRequest {
    private String title;
    private String description;
    private Long columnId;
    private Long dueTo;
    private Boolean completed;
    private Difficulty difficulty;
    private List<Long> tagIds;
    private List<Long> employeeIds;
}
