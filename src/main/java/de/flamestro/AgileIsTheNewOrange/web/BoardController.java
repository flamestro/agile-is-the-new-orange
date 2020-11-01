package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.service.BoardService;
import de.flamestro.AgileIsTheNewOrange.board.service.LaneService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final LaneService laneService;

    // TODO: Think of a better mapping -> get by name might not be the best way for /board
    @GetMapping
    public ResponseEntity<List<Board>> getBoardByName(@Param("name") String name){
        List<Board> board =  boardService.getBoardByName(name);
        return ResponseEntity.ok(board);
    }

    @PostMapping
    public ResponseEntity<Board> createBoard(@Param("name") String name){
        Board board = boardService.createBoard(name);
        return ResponseEntity.ok(board);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable String id){
        Board board = boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Board> removeBoardById(@PathVariable String id){
        boardService.removeBoard(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId}/lane")
    public ResponseEntity<Lane> addLaneToBoard(@PathVariable String boardId, @Param("name") String name){
        Lane lane = laneService.createLane(name, boardId);
        return ResponseEntity.ok(lane);
    }

    @DeleteMapping("/{boardId}/lane/{laneId}")
    public ResponseEntity<Lane> removeLaneFromBoard(@PathVariable String boardId, @PathVariable String laneId){
        laneService.removeLane(laneId, boardId);
        return ResponseEntity.ok().build();
    }
}
