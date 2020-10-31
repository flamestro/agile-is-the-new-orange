package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.Board;
import de.flamestro.AgileIsTheNewOrange.board.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    public ResponseEntity<List<Board>> getBoard(@Param("id") String id){
        List<Board> board =  boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

    @PostMapping("/board")
    public ResponseEntity<Board> createBoard(@Param("name") String name){
        Board board = boardService.createBoard(name);
        return ResponseEntity.ok(board);
    }
}
