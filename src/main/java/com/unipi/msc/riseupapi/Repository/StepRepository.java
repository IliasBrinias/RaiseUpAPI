package com.unipi.msc.riseupapi.Repository;

import com.unipi.msc.riseupapi.Model.Board;
import com.unipi.msc.riseupapi.Model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepRepository extends JpaRepository<Step,Long> {
    List<Step> findAllByBoardOrderByPositionAsc(Board board);
}