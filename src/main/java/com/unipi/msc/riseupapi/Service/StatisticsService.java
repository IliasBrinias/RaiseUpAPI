package com.unipi.msc.riseupapi.Service;

import com.unipi.msc.riseupapi.Interface.IStatistics;
import com.unipi.msc.riseupapi.Interface.ITag;
import com.unipi.msc.riseupapi.Model.Tag;
import com.unipi.msc.riseupapi.Model.Task;
import com.unipi.msc.riseupapi.Repository.BoardRepository;
import com.unipi.msc.riseupapi.Repository.TagRepository;
import com.unipi.msc.riseupapi.Repository.TaskRepository;
import com.unipi.msc.riseupapi.Repository.UserRepository;
import com.unipi.msc.riseupapi.Request.StatisticsRequest;
import com.unipi.msc.riseupapi.Request.TagRequest;
import com.unipi.msc.riseupapi.Response.GenericResponse;
import com.unipi.msc.riseupapi.Response.ProgressPresenter;
import com.unipi.msc.riseupapi.Response.TagPresenter;
import com.unipi.msc.riseupapi.Response.TaskPresenter;
import com.unipi.msc.riseupapi.Shared.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticsService implements IStatistics {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;
    private static final LocalTime TIME = LocalTime.of(0, 0);

    @Override
    public ResponseEntity<?> getStatistics(StatisticsRequest request) {
        if (request.getDateFrom() == null || request.getDateTo() == null){
            return GenericResponse.builder().message(ErrorMessages.COMPLETE_THE_MANDATORY_FIELDS).build().badRequest();
        }

        List<Task> tasks = taskRepository.findAllByDueToBetween(request.getDateFrom(),request.getDateTo());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date(request.getDateTo()));
        ZonedDateTime zdt = ZonedDateTime.of(c.get(Calendar.YEAR),
                                             c.get(Calendar.MONTH),
                                             c.get(Calendar.DAY_OF_MONTH),
                                             c.getMinimum(Calendar.HOUR),
                                             c.getMinimum(Calendar.MINUTE),
                                             c.getMinimum(Calendar.SECOND),
                                             c.getMinimum(Calendar.MILLISECOND),
                                             ZoneId.systemDefault()).with(TIME);

        List<ProgressPresenter> presenters = new ArrayList<>();
        while (true){
            long timestampMillis = zdt.toInstant().toEpochMilli();
            Calendar calendarEndOfDay = getEndOfDay(timestampMillis);
            if (timestampMillis< request.getDateFrom()) break;
            Long countOpenTasks = tasks.stream()
                    .filter(task -> task.getDueTo() > timestampMillis)
                    .filter(task -> task.getDueTo() < calendarEndOfDay.getTimeInMillis())
                    .filter(Task::isCompleted)
                    .count();
            presenters.add(ProgressPresenter.builder().date(timestampMillis).openTasks(countOpenTasks).build());
            zdt = zdt.plusDays(-1);
        }

        return GenericResponse.builder().data(presenters).build().success();
    }

    @Override
    public ResponseEntity<?> getUsersStatistics(StatisticsRequest request) {

        return null;
    }

    @Override
    public ResponseEntity<?> getUserStatistics(Long userId, StatisticsRequest request) {
        return null;
    }

    private Calendar getEndOfDay(long timestampMillis) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(timestampMillis));
        c.set(Calendar.HOUR,c.getMaximum(Calendar.HOUR));
        c.set(Calendar.MINUTE,c.getMaximum(Calendar.MINUTE));
        c.set(Calendar.MILLISECOND,c.getMaximum(Calendar.MILLISECOND));
        return c;
    }
}
