package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
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

    // TODO: Think of a better mapping -> get by name might not be the best way for /board
    @GetMapping("/board")
    public ResponseEntity<List<Board>> getBoardByName(@Param("name") String name){
        List<Board> board =  boardService.getBoardByName(name);
        return ResponseEntity.ok(board);
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable String id){
        Board board = boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

    @PostMapping("/board")
    public ResponseEntity<Board> createBoard(@Param("name") String name){
        Board board = boardService.createBoard(name);
        return ResponseEntity.ok(board);
    }
}
