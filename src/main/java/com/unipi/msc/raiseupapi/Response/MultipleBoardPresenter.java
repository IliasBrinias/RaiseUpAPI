package com.unipi.msc.raiseupapi.Response;

import com.unipi.msc.raiseupapi.Model.Board;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultipleBoardPresenter {
    private Long id;
    private String title;
    private Long date;
    private List<UserPresenter> employees = new ArrayList<>();
    private Long totalTasks;

    public static MultipleBoardPresenter getPresenter(Board board){
        MultipleBoardPresenter presenter = MultipleBoardPresenter.builder()
                .id(board.getId())
                .title(board.getTitle())
                .date(board.getDate())
                .employees(UserPresenter.getPresenter(board.getUsers()))
                .build();
        AtomicReference<Long> totalTasks = new AtomicReference<>(0L);
        board.getSteps().forEach(column-> totalTasks.updateAndGet(v -> v + column.getTasks().size()));
        presenter.setTotalTasks(totalTasks.get());
        return presenter;
    }
}
