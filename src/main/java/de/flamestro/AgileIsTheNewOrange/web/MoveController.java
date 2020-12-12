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
    public ResponseEntity<LaneResponse> moveCard(@RequestBody MoveCardRequest request) {
        laneService.moveCard(
                cardService.getCardById(request.getSourceCardId()),
                laneService.getLaneById(request.getSourceLaneId()),
                boardService.getBoardById(request.getSourceBoardId()),
                cardService.getCardById(request.getTargetCardId()),
                laneService.getLaneById(request.getTargetLaneId()),
                boardService.getBoardById(request.getTargetBoardId()));
        return ResponseEntity.ok(LaneResponse.builder().status(Status.SUCCESS).build());
    }
}
