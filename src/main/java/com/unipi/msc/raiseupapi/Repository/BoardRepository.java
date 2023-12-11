package com.unipi.msc.raiseupapi.Repository;

import com.unipi.msc.raiseupapi.Model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long> {
}