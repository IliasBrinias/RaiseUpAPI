package com.unipi.msc.raiseupapi.Controller;

import com.unipi.msc.raiseupapi.Interface.IBoard;
import com.unipi.msc.raiseupapi.Request.BoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardRequest request){
        return iBoard.createBoard(request);
    }
}
