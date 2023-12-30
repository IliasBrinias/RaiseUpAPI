package com.unipi.msc.raiseupapi.Service;

import com.unipi.msc.raiseupapi.Interface.IBoard;
import com.unipi.msc.raiseupapi.Model.Board;
import com.unipi.msc.raiseupapi.Model.Step;
import com.unipi.msc.raiseupapi.Model.User;
import com.unipi.msc.raiseupapi.Repository.BoardRepository;
import com.unipi.msc.raiseupapi.Repository.StepRepository;
import com.unipi.msc.raiseupapi.Repository.UserRepository;
import com.unipi.msc.raiseupapi.Request.BoardRequest;
import com.unipi.msc.raiseupapi.Response.*;
import com.unipi.msc.raiseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
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
        for (int i = 0; i < request.getColumns().size(); i++) {
            Step step = stepRepository.save(Step.builder()
                    .title(request.getColumns().get(i))
                    .position((long) i)
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

    @Override
    public ResponseEntity<?> updateBoard(Long boardId, BoardRequest request) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return  GenericResponse.builder().message(ErrorMessages.BOARD_NOT_FOUND).build().badRequest();

//        if ()
        return null;
    }

    @Override
    public ResponseEntity<?> getBoardColumns(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return  GenericResponse.builder().message(ErrorMessages.BOARD_NOT_FOUND).build().badRequest();
        List<StepPresenter> presenter = new ArrayList<>();
        for (Step step:board.getSteps().stream().sorted(Comparator.comparingLong(Step::getPosition)).toList()){
            presenter.add(StepPresenter.getPresenterWithoutTask(step));
        }
        return GenericResponse.builder().data(presenter).build().success();
    }

    @Override
    public ResponseEntity<?> searchBoards(String keyword) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> users = new ArrayList<>();
        users.add(user);
        List<Board> boards;
        if (keyword.isEmpty()) {
            boards = boardRepository.findAllByUsersIn(users);
        }else{
            boards = boardRepository.findAllByUsersInAndTitleContaining(users,keyword);
        }
        List<MultipleBoardPresenter> presenters = MultipleBoardPresenter.getPresenter(boards);
        return GenericResponse.builder().data(presenters).build().success();
    }
}
