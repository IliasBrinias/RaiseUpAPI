package com.unipi.msc.riseupapi.Repository;

import com.unipi.msc.riseupapi.Model.Board;
import com.unipi.msc.riseupapi.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
    List<Board> findAllByUsersInOrOwner(List<User> users, User user);
    List<Board> findAllByUsersInOrOwnerIsInAndTitleContainingIgnoreCase(List<User> users, List<User> owners, String title);
    long countAllByOwnerIsOrUsersIn(User owner, List<User> users);
}