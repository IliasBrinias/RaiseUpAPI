package com.unipi.msc.riseupapi.Interface;

import com.unipi.msc.riseupapi.Request.BoardRequest;
import com.unipi.msc.riseupapi.Request.ColumnRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBoard {
    ResponseEntity<?> getBoards();
    ResponseEntity<?> getBoard(Long boardId);

    ResponseEntity<?> createBoard(BoardRequest request);

    ResponseEntity<?> getBoardEmployees(Long boardId);

    ResponseEntity<?> updateBoard(Long boardId, BoardRequest request);

    ResponseEntity<?> getBoardSteps(Long boardId);

    ResponseEntity<?> searchBoards(String keyword);

    ResponseEntity<?> addBoardStep(Long boardId, ColumnRequest request);

    ResponseEntity<?> deleteBoard(Long boardId);

    ResponseEntity<?> changeColumnOrder(Long boardId, List<ColumnRequest> request);
}
