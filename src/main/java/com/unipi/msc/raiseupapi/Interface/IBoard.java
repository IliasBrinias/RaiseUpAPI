package com.unipi.msc.raiseupapi.Interface;

import org.springframework.http.ResponseEntity;

public interface IBoard {
    ResponseEntity<?> getBoards();
    ResponseEntity<?> getBoard(Long boardId);
}
