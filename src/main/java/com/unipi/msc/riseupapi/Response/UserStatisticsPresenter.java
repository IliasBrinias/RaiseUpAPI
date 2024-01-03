package com.unipi.msc.riseupapi.Response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserStatisticsPresenter {
    private UserPresenter userPresenter;
    private Long status;
}
