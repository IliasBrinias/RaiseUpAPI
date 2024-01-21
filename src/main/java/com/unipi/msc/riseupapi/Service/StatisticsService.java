package com.unipi.msc.riseupapi.Service;

import com.unipi.msc.riseupapi.Interface.IStatistics;
import com.unipi.msc.riseupapi.Model.Admin;
import com.unipi.msc.riseupapi.Model.Board;
import com.unipi.msc.riseupapi.Model.Task;
import com.unipi.msc.riseupapi.Model.User;
import com.unipi.msc.riseupapi.Repository.BoardRepository;
import com.unipi.msc.riseupapi.Repository.TaskRepository;
import com.unipi.msc.riseupapi.Repository.UserRepository;
import com.unipi.msc.riseupapi.Response.GenericResponse;
import com.unipi.msc.riseupapi.Response.ProgressPresenter;
import com.unipi.msc.riseupapi.Response.UserPresenter;
import com.unipi.msc.riseupapi.Response.UserStatisticsPresenter;
import com.unipi.msc.riseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StatisticsService implements IStatistics {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;
    private static final LocalTime TIME = LocalTime.of(2, 0);

    @Override
    public ResponseEntity<?> getStatistics(Long dateFrom, Long dateTo) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (dateFrom == null || dateTo == null){
            return GenericResponse.builder().message(ErrorMessages.COMPLETE_THE_MANDATORY_FIELDS).build().badRequest();
        }

        List<Task> tasks;
        if (user instanceof Admin){
            tasks = taskRepository.findAllByDueToBetweenAndCompletedIsTrue(dateFrom,dateTo);
        }else {
            List<User> users = new ArrayList<>();
            users.add(user);
            tasks = taskRepository.findAllByDueToBetweenAndCompletedIsTrueAndUsersIn(dateFrom,dateTo,users);
        }
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(dateFrom));
        ZonedDateTime zdt = ZonedDateTime.of(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0,
                0,
                ZoneId.systemDefault()).with(TIME);

        List<ProgressPresenter> presenters = new ArrayList<>();
        while (true){
            long startOfDay = getStartOfDay(zdt.toInstant().toEpochMilli());
            long endOfDay = getEndOfDay(startOfDay);
            if (startOfDay > dateTo) break;
            Long countCompletedTasks = tasks.stream()
                    .filter(task -> task.getDueTo() >= startOfDay)
                    .filter(task -> task.getDueTo() <= endOfDay)
                    .count();
            presenters.add(ProgressPresenter.builder().date(startOfDay).completedTasks(countCompletedTasks).build());
            zdt = zdt.plusDays(1);
        }
        return GenericResponse.builder().data(presenters).build().success();
    }
    @Override
    public ResponseEntity<?> getUsersStatistics(Long dateFrom, Long dateTo) {
        List<UserStatisticsPresenter> userStatisticsPresenters = new ArrayList<>();
        User signedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (signedUser instanceof Admin){
            List<User> users = userRepository.findAll();
            for (User user:users){
                UserStatisticsPresenter userStatisticsPresenter = new UserStatisticsPresenter();
                userStatisticsPresenter.setUser(UserPresenter.getPresenter(user));
                userStatisticsPresenter.setCompletedTask(user.getTasks().stream().filter(Task::isCompleted).count());
                userStatisticsPresenters.add(userStatisticsPresenter);
            }
            userStatisticsPresenters = userStatisticsPresenters.stream().sorted(Comparator.comparingLong(UserStatisticsPresenter::getCompletedTask).reversed()).collect(Collectors.toList());
        }else {
            UserStatisticsPresenter userStatisticsPresenter = new UserStatisticsPresenter();
            userStatisticsPresenter.setUser(UserPresenter.getPresenter(signedUser));

            List<User> users = new ArrayList<>();
            users.add(signedUser);
            userStatisticsPresenter.setCompletedTask(taskRepository.countAllByUsersIn(users));
            userStatisticsPresenter.setBoards(boardRepository.countAllByOwnerIsOrUsersIn(signedUser,users));

            userStatisticsPresenters.add(userStatisticsPresenter);
        }
        return GenericResponse.builder().data(userStatisticsPresenters).build().success();
    }

    @Override
    public ResponseEntity<?> getUserStatistics(Long userId, Long dateFrom, Long dateTo) {
//        Map<Long,Long> completeBoardTasks = new HashMap<>();
//        Map<Long,Long> boardOpenTasks = new HashMap<>();
//        Map<Long,String> boardName = new HashMap<>();
//        UserStatisticsPresenter userStatisticsPresenter = new UserStatisticsPresenter();
//        Long completedTasks = 0L;
//
//        User user = userRepository.findById(userId).orElse(null);
//        if (user == null) return GenericResponse.builder().message(ErrorMessages.USER_NOT_FOUND).build().badRequest();
//
//        userStatisticsPresenter.setUser(UserPresenter.getPresenter(user));
//        for (Task task: user.getTasks()){
//            Board board = task.getStep().getBoard();
//            if (!boardName.containsKey(board.getId())){
//                boardName.put(board.getId(),board.getTitle());
//            }
//            if (task.isCompleted()){
//                completeBoardTasks.merge(board.getId(),1L,Long::sum);
//            }else {
//                boardOpenTasks.merge(board.getId(),1L,Long::sum);
//            }
//        }
//
//        for(Map.Entry<Long,String> boardEntry:boardName.entrySet()){
//            UserBoardStatisticsPresenter userBoardStatisticsPresenter = new UserBoardStatisticsPresenter();
//            userBoardStatisticsPresenter.setBoardId(boardEntry.getKey());
//            userBoardStatisticsPresenter.setBoardName(boardEntry.getValue());
//            userBoardStatisticsPresenter.setOpenTasks(boardOpenTasks.getOrDefault(boardEntry.getKey(),0L));
//            userBoardStatisticsPresenter.setCompletedTasks(boardOpenTasks.getOrDefault(boardEntry.getKey(),0L));
//            completedTasks += userBoardStatisticsPresenter.getCompletedTasks();
//            userStatisticsPresenter.getUserBoard().add(userBoardStatisticsPresenter);
//        }
//
//        userStatisticsPresenter.setCompletedTask(completedTasks);
//
        return GenericResponse.builder().build().badRequest();
    }
    private long getStartOfDay(long timestampMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestampMillis);
        c.set(Calendar.HOUR,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.MILLISECOND,0);
        return c.getTimeInMillis();
    }
    private Long getEndOfDay(long timestampMillis) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(timestampMillis));
        c.set(Calendar.HOUR,23);
        c.set(Calendar.MINUTE,99);
        c.set(Calendar.MILLISECOND,99);
        return c.getTimeInMillis();
    }
}
