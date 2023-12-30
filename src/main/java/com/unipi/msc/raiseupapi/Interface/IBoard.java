package com.unipi.msc.raiseupapi.Interface;

import com.unipi.msc.raiseupapi.Request.BoardRequest;
import org.springframework.http.ResponseEntity;

public interface IBoard {
    ResponseEntity<?> getBoards();
    ResponseEntity<?> getBoard(Long boardId);

    ResponseEntity<?> createBoard(BoardRequest request);

    ResponseEntity<?> getBoardEmployees(Long boardId);

    ResponseEntity<?> updateBoard(Long boardId, BoardRequest request);

    ResponseEntity<?> getBoardColumns(Long boardId);

    ResponseEntity<?> searchBoards(String keyword);
}
