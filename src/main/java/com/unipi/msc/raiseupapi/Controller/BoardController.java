package com.unipi.msc.raiseupapi.Controller;

import com.unipi.msc.raiseupapi.Interface.IBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {

    private final IBoard iBoard;

    @GetMapping
    public ResponseEntity<?> getBoards(){
        return iBoard.getBoards();
    }

    @GetMapping("{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId){
        return iBoard.getBoard(boardId);
    }
}
