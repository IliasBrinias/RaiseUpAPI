package com.unipi.msc.riseupapi.Controller;

import com.unipi.msc.riseupapi.Interface.IBoard;
import com.unipi.msc.riseupapi.Request.BoardRequest;
import com.unipi.msc.riseupapi.Request.ColumnRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("{boardId}/steps")
    public ResponseEntity<?> getBoardSteps(@PathVariable Long boardId){
        return iBoard.getBoardSteps(boardId);
    }
    @PostMapping("{boardId}/steps")
    public ResponseEntity<?> addBoardStep(@PathVariable Long boardId,@RequestBody ColumnRequest request){return iBoard.addBoardStep(boardId,request);}
    @PostMapping
    public ResponseEntity<?> createBoard(@RequestBody BoardRequest request){
        return iBoard.createBoard(request);
    }
    @PatchMapping("{boardId}")
    public ResponseEntity<?> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequest request){return iBoard.updateBoard(boardId, request);}
    @PatchMapping("{boardId}/column-order")
    public ResponseEntity<?> changeColumnOrder(@PathVariable Long boardId, @RequestBody List<ColumnRequest> request){return iBoard.changeColumnOrder(boardId, request);}

    @DeleteMapping("{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId){return iBoard.deleteBoard(boardId);}
}
