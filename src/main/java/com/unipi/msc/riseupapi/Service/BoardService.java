package com.unipi.msc.riseupapi.Service;

import com.unipi.msc.riseupapi.Interface.IBoard;
import com.unipi.msc.riseupapi.Interface.IStep;
import com.unipi.msc.riseupapi.Model.Board;
import com.unipi.msc.riseupapi.Model.Step;
import com.unipi.msc.riseupapi.Model.Task;
import com.unipi.msc.riseupapi.Model.User;
import com.unipi.msc.riseupapi.Repository.BoardRepository;
import com.unipi.msc.riseupapi.Repository.StepRepository;
import com.unipi.msc.riseupapi.Repository.TaskRepository;
import com.unipi.msc.riseupapi.Repository.UserRepository;
import com.unipi.msc.riseupapi.Request.BoardRequest;
import com.unipi.msc.riseupapi.Request.ColumnRequest;
import com.unipi.msc.riseupapi.Response.*;
import com.unipi.msc.riseupapi.Shared.ErrorMessages;
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
    private final TaskRepository taskRepository;
    private final IStep iStep;
    @Override
    public ResponseEntity<?> getBoards() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> users = new ArrayList<>();
        users.add(user);
        List<Board> boards = boardRepository.findAllByUsersInOrOwner(users,user);
        List<BoardPresenter> presenters = BoardPresenter.getPresenterWithoutSteps(boards);
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
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
                .owner(owner)
                .build());

        steps.forEach(step -> {
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
        if (request.getTitle()!=null) {
            board.setTitle(request.getTitle());
            board = boardRepository.save(board);
        }
        if (request.getEmployeesId()!=null){
            List<User> newUsers = userRepository.findUsersByIdIn(request.getEmployeesId()).orElse(null);
            if (newUsers == null) return GenericResponse.builder().message(ErrorMessages.USER_NOT_FOUND).build().badRequest();
            for (User user: board.getUsers()){
                if (!newUsers.contains(user)){
                    // remove Employee from Tasks
                    List<Task> userTaskForRemove = new ArrayList<>();
                    for (Task task:user.getTasks()){
                        if (task.getStep().getBoard() == board){
                            task.getUsers().remove(user);
                            task = taskRepository.save(task);
                            userTaskForRemove.add(task);
                        }
                    }
                    if (!userTaskForRemove.isEmpty()) {
                        user.getTasks().removeAll(userTaskForRemove);
                        user = userRepository.save(user);
                    }

                    // remove Employee from Board
                    user.getBoards().remove(board);
                    userRepository.save(user);
                }
            }
            board.setUsers(newUsers);
            board = boardRepository.save(board);
            for (User user:newUsers){
                if (!user.getBoards().contains(board)){
                    user.getBoards().add(board);
                    userRepository.save(user);
                }
            }
        }
        return GenericResponse.builder().data(BoardPresenter.getPresenter(board)).build().success();
    }
    @Override
    public ResponseEntity<?> getBoardSteps(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return  GenericResponse.builder().message(ErrorMessages.BOARD_NOT_FOUND).build().badRequest();
        List<ColumnPresenter> presenter = new ArrayList<>();
        for (Step step :board.getSteps().stream().sorted(Comparator.comparingLong(Step::getPosition)).toList()){
            presenter.add(ColumnPresenter.getPresenterWithoutTask(step));
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
            boards = boardRepository.findAllByUsersInOrOwner(users,user);
        }else{
            boards = boardRepository.findAllByUsersInAndTitleContaining(users,keyword);
        }
        List<BoardPresenter> presenters = BoardPresenter.getPresenterWithoutSteps(boards);
        return GenericResponse.builder().data(presenters).build().success();
    }

    @Override
    public ResponseEntity<?> addBoardStep(Long boardId, ColumnRequest request) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return  GenericResponse.builder().message(ErrorMessages.BOARD_NOT_FOUND).build().badRequest();
        long position;
        if (request.getPosition() != null){
            position = request.getPosition();
        }else{
            position = board.getSteps().stream().map(Step::getPosition).max(Long::compareTo).orElse(-1L) + 1;
        }
        Step step = stepRepository.save(Step.builder()
                .title(request.getTitle())
                .position(position)
                .build());
        board.getSteps().add(step);
        board = boardRepository.save(board);
        step.setBoard(board);
        stepRepository.save(step);
        return GenericResponse.builder().data(BoardPresenter.getPresenter(board)).build().success();
    }

    @Override
    public ResponseEntity<?> deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return  GenericResponse.builder().message(ErrorMessages.BOARD_NOT_FOUND).build().badRequest();

        for (Step step : board.getSteps()) {
            ResponseEntity<?> response = iStep.deleteStep(step.getId());
            if (response.getStatusCode().value() != 200) return response;
        }
        board.getSteps().clear();
        board = boardRepository.save(board);

        for (User user:board.getUsers()){
            user.getBoards().remove(board);
            userRepository.save(user);
        }
        board.getUsers().clear();
        board = boardRepository.save(board);

        boardRepository.delete(board);

        return GenericResponse.builder().build().success();
    }

    @Override
    public ResponseEntity<?> changeColumnOrder(Long boardId, List<ColumnRequest> request) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null) return  GenericResponse.builder().message(ErrorMessages.BOARD_NOT_FOUND).build().badRequest();
        board.getSteps().forEach(step -> {
            ColumnRequest columnRequest = request.stream().filter(cr -> cr.getId().equals(step.getId())).findFirst().orElse(null);
            if (columnRequest == null) return;
            step.setPosition(columnRequest.getPosition());
            stepRepository.save(step);
        });
        return GenericResponse.builder().build().success();
    }
}