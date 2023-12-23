package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Interface.IBoard;
import com.unipi.msc.raiseupapi.Model.Board;
import com.unipi.msc.raiseupapi.Model.Employee;
import com.unipi.msc.raiseupapi.Model.Step;
import com.unipi.msc.raiseupapi.Model.User;
import com.unipi.msc.raiseupapi.Repository.BoardRepository;
import com.unipi.msc.raiseupapi.Repository.StepRepository;
import com.unipi.msc.raiseupapi.Repository.UserRepository;
import com.unipi.msc.raiseupapi.Request.BoardRequest;
import com.unipi.msc.raiseupapi.Response.BoardPresenter;
import com.unipi.msc.raiseupapi.Response.GenericResponse;
import com.unipi.msc.raiseupapi.Response.MultipleBoardPresenter;
import com.unipi.msc.raiseupapi.Response.UserPresenter;
import com.unipi.msc.raiseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BoardService implements IBoard {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final StepRepository stepRepository;

    @Override
    public ResponseEntity<?> getBoards() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> users = new ArrayList<>();
        users.add(user);
        List<Board> boards = boardRepository.findAllByUsersIn(users);
        List<MultipleBoardPresenter> presenters = MultipleBoardPresenter.getPresenter(boards);
        return GenericResponse.builder().data(presenters).build().success();
    }

    @Override
    public ResponseEntity<?> getBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return GenericResponse.builder().message(ErrorMessages.BOARD_NOT_FOUND).build().badRequest();
        return GenericResponse.builder().data(BoardPresenter.getPresenter(board)).build().success();
    }

    @Override
    public ResponseEntity<?> createBoard(BoardRequest request) {
        List<User> users = userRepository.findUsersByIdIn(request.getEmployeesId()).orElse(null);
        if (users == null) return GenericResponse.builder().message(ErrorMessages.USER_NOT_FOUND).build().badRequest();
        if (request.getColumns() == null) return GenericResponse.builder().message(ErrorMessages.NO_COLUMNS_FOUND).build().badRequest();

        List<Step> steps = new ArrayList<>();
        for (String column : request.getColumns()){
            Step step = stepRepository.save(Step.builder()
                    .title(column)
                    .build());
            steps.add(step);
        }

        Board board = boardRepository.save(Board.builder()
                .title(request.getTitle())
                .date(new Date().getTime())
                .steps(steps)
                .users(users)
                .build());

        steps.forEach( step -> {
            step.setBoard(board);
            stepRepository.save(step);
        });

        for (User user : users) {
            user.getBoards().add(board);
            userRepository.save(user);
        }

        return GenericResponse.builder().data(BoardPresenter.getPresenter(board)).build().success();
    }

    @Override
    public ResponseEntity<?> getBoardEmployees(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return  GenericResponse.builder().message(ErrorMessages.BOARD_NOT_FOUND).build().badRequest();
        List<UserPresenter> presenter = UserPresenter.getPresenter(board.getUsers());
        return GenericResponse.builder().data(presenter).build().success();
    }
}
