package com.unipi.msc.riseupapi.Response;

import com.unipi.msc.riseupapi.Model.Board;
import com.unipi.msc.riseupapi.Model.Step;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardPresenter {
    private Long id;
    private String title;
    private Long date;
    private List<ColumnPresenter> steps = new ArrayList<>();
    private List<UserPresenter> users = new ArrayList<>();
    private Long totalTasks;
    private UserPresenter owner;
    public static BoardPresenter getPresenter(Board board){
        List<Step> sortedSteps = board.getSteps().stream().sorted(Comparator.comparingLong(Step::getPosition)).collect(Collectors.toList());
        BoardPresenter presenter = BoardPresenter.builder()
                .id(board.getId())
                .title(board.getTitle())
                .date(board.getDate())
                .steps(ColumnPresenter.getPresenter(sortedSteps))
                .users(UserPresenter.getPresenter(board.getUsers()))
                .owner(board.getOwner() != null ? UserPresenter.getPresenter(board.getOwner()) : null)
                .build();
        AtomicReference<Long> totalTasks = new AtomicReference<>(0L);
        board.getSteps().forEach(column-> {
            if (column.getTasks() == null) return;
            totalTasks.updateAndGet(v -> v + column.getTasks().size());
        });
        presenter.setTotalTasks(totalTasks.get());
        return presenter;
    }
    public static BoardPresenter getPresenterWithoutSteps(Board board){
        BoardPresenter presenter = BoardPresenter.builder()
                .id(board.getId())
                .title(board.getTitle())
                .date(board.getDate())
                .users(UserPresenter.getPresenter(board.getUsers()))
                .owner(UserPresenter.getPresenter(board.getOwner()))
                .build();
        AtomicReference<Long> totalTasks = new AtomicReference<>(0L);
        board.getSteps().forEach(column-> totalTasks.updateAndGet(v -> v + column.getTasks().size()));
        presenter.setTotalTasks(totalTasks.get());
        return presenter;
    }
    public static List<BoardPresenter> getPresenterWithoutSteps(List<Board> boards){
        List<BoardPresenter> presenters = new ArrayList<>();
        boards.forEach(board -> presenters.add(BoardPresenter.getPresenterWithoutSteps(board)));
        return presenters;
    }
}
