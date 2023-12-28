package com.unipi.msc.raiseupapi.Repository;

import com.unipi.msc.raiseupapi.Model.Board;
import com.unipi.msc.raiseupapi.Model.Task;
import com.unipi.msc.raiseupapi.Model.User;
import com.unipi.msc.raiseupapi.Request.TaskRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findAllByUsersIn(List<User> users);
}