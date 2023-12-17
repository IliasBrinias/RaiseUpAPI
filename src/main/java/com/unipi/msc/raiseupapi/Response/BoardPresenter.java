package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Model.Board;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardPresenter {
    private Long id;
    private String title;
    private Long date;
    private List<StepPresenter> steps = new ArrayList<>();
    private List<UserPresenter> users = new ArrayList<>();
    public static BoardPresenter getPresenter(Board board){
        return BoardPresenter.builder()
                .id(board.getId())
                .title(board.getTitle())
                .date(board.getDate())
                .steps(StepPresenter.getPresenter(board.getSteps()))
                .users(UserPresenter.getPresenter(board.getUsers()))
                .build();
    }
}
