package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.service.BoardService;
import de.flamestro.AgileIsTheNewOrange.board.service.MoveService;
import de.flamestro.AgileIsTheNewOrange.web.model.LaneResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.MoveCardRequest;
import de.flamestro.AgileIsTheNewOrange.web.model.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class MoveController {

    private final BoardService boardService;

    @PutMapping("/moveCard")
    public ResponseEntity<LaneResponse> moveCard(@RequestBody MoveCardRequest moveCardRequest) {
        Board sourceBoard = boardService.getBoardById(moveCardRequest.getSourceBoardId());
        Board targetBoard = boardService.getBoardById(moveCardRequest.getTargetBoardId());
        MoveService.moveCardFromSourceToTarget(sourceBoard, targetBoard, moveCardRequest);
        boardService.saveBoard(sourceBoard);
        boardService.saveBoard(targetBoard);
        return ResponseEntity.ok(LaneResponse.builder().status(Status.SUCCESS).build());
    }
}
