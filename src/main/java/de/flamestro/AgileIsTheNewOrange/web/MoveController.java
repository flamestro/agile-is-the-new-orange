package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.service.BoardService;
import de.flamestro.AgileIsTheNewOrange.board.service.CardService;
import de.flamestro.AgileIsTheNewOrange.board.service.LaneService;
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
    private final LaneService laneService;
    private final CardService cardService;

    @PutMapping("/moveCard")
    public ResponseEntity<LaneResponse> moveCard(@RequestBody MoveCardRequest moveCardRequest) {
        laneService.moveCard(
                cardService.getCardById(moveCardRequest.getSourceCardId()),
                laneService.getLaneById(moveCardRequest.getSourceLaneId()),
                boardService.getBoardById(moveCardRequest.getSourceBoardId()),
                cardService.getCardById(moveCardRequest.getTargetCardId()),
                laneService.getLaneById(moveCardRequest.getTargetLaneId()),
                boardService.getBoardById(moveCardRequest.getTargetBoardId()));
        return ResponseEntity.ok(LaneResponse.builder().status(Status.SUCCESS).build());
    }
}
