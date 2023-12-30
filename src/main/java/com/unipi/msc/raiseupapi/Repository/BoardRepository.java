package com.unipi.msc.raiseupapi.Repository;

import com.unipi.msc.raiseupapi.Model.Board;
import com.unipi.msc.raiseupapi.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByUsersIn(List<User> users);
    List<Board> findAllByUsersInAndTitleContaining(List<User> users, String title);
}