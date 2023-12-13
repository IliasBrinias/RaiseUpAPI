package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Interface.IBoard;
import com.unipi.msc.raiseupapi.Repository.BoardRepository;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Response.MultipleBoardPresenter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Component
@RequiredArgsConstructor
public class BoardService implements IBoard {
    private final BoardRepository boardRepository;

    @Override
    public ResponseEntity<?> getBoards() {
        List<MultipleBoardPresenter> presenters = new ArrayList<>();
        boardRepository.findAll().forEach(board -> {
            AtomicReference<Long> numberOfTask = new AtomicReference<>(0L);
            board.getColumns().forEach(column-> numberOfTask.updateAndGet(v -> v + column.getTasks().size()));
            presenters.add(new MultipleBoardPresenter(board.getId(),board.getTitle(),board.getDate(),null,5L,numberOfTask.get()));
        });
        return GenericResponse.builder().data(presenters).build().success();
    }

    @Override
    public ResponseEntity<?> getBoard(Long boardId) {
        return null;
    }
}
