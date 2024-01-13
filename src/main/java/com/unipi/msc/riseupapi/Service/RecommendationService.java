package com.unipi.msc.riseupapi.Service;

import com.google.common.base.Functions;
import com.unipi.msc.riseupapi.Interface.IRecommendation;
import com.unipi.msc.riseupapi.Model.Task;
import com.unipi.msc.riseupapi.Model.User;
import com.unipi.msc.riseupapi.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecommendationService implements IRecommendation {
    private final TaskRepository taskRepository;
    @Override
    public List<User> recommendUsers(Task task) {
        List<User> users = new ArrayList<>();
        List<Task> tasks;
        if (task.getDifficulty() == null){
            tasks = taskRepository.findAllByTagsIn(task.getTags());
        }else {
            tasks = taskRepository.findAllByTagsInAndDifficultyIs(task.getTags(),task.getDifficulty());
        }
        tasks.forEach(t -> users.addAll(t.getUsers()));
        return users.stream()
                .collect(Collectors.groupingBy(Functions.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .limit(5)
                .collect(Collectors.toList());
    }
}
