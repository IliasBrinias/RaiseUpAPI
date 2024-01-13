package com.unipi.msc.riseupapi.Repository;

import com.unipi.msc.riseupapi.Model.Difficulty;
import com.unipi.msc.riseupapi.Model.Tag;
import com.unipi.msc.riseupapi.Model.Task;
import com.unipi.msc.riseupapi.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findAllByUsersIn(List<User> users);
    List<Task> findAllByUsersInAndTitleContaining(List<User> users, String title);
    List<Task> findAllByDueToBetweenAndCompletedIsTrue(Long dateFrom, Long dateTo);
    List<Task> findAllByDueToBetweenAndCompletedIsTrueAndUsersIn(Long dateFrom, Long dateTo, List<User> users);
    long countAllByUsersIn(List<User> users);
    List<Task> findAllByTagsIn(List<Tag> tags);
    List<Task> findAllByTagsInAndDifficultyIs(List<Tag> tags, Difficulty difficulty);
}