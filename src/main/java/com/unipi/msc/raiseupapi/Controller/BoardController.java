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
    @GetMapping("search")
    public ResponseEntity<?> searchBoards(@RequestParam String keyword){
        return iBoard.searchBoards(keyword);
    }
    @GetMapping("{boardId}/employees")
    public ResponseEntity<?> getBoardEmployees(@PathVariable Long boardId){
        return iBoard.getBoardEmployees(boardId);
    }
    @GetMapping("{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable Long boardId){
        return iBoard.getBoard(boardId);
    }
    @GetMapping("{boardId}/columns")
    public ResponseEntity<?> getBoardColumns(@PathVariable Long boardId){
        return iBoard.getBoardColumns(boardId);
    }
    @PatchMapping("{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequest request){
        return iBoard.updateBoard(boardId, request);
    }
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardRequest request){
        return iBoard.createBoard(request);
    }
}
