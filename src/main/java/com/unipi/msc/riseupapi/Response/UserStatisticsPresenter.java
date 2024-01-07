package com.unipi.msc.riseupapi.Response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatisticsPresenter {
    private UserPresenter user;
    private Long completedTask;
    private List<UserBoardStatisticsPresenter> userBoard;
}
