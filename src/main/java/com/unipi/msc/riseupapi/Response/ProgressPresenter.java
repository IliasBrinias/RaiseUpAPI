package com.unipi.msc.riseupapi.Response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressPresenter {
    private Long date;
    private Long openTasks;
}
