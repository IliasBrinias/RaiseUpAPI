package com.unipi.msc.riseupapi.Response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBoardStatisticsPresenter {
    private Long boardId;
    private String boardName;
    private Long openTasks;
    private Long completedTasks;
}
