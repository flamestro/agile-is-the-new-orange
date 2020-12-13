package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.service.BoardService;
import de.flamestro.AgileIsTheNewOrange.board.service.CardService;
import de.flamestro.AgileIsTheNewOrange.board.service.LaneService;
import de.flamestro.AgileIsTheNewOrange.web.model.BoardResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.CardResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.LaneResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/board")
@AllArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final LaneService laneService;
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@Param("name") String name,
                                                     @Param("userId") String userId) {
        Board board = boardService.createBoard(name, userId);
        return ResponseEntity.ok(BoardResponse.builder().boards(List.of(board)).status(Status.SUCCESS).build());
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<BoardResponse> deleteBoard(@PathVariable String boardId) {
        Board board = boardService.removeBoard(boardService.getBoardById(boardId));
        return ResponseEntity.ok(BoardResponse.builder().boards(List.of(board)).status(Status.SUCCESS).build());
    }

    @GetMapping
    public ResponseEntity<BoardResponse> getBoardsByUserId(@Param("userId") String userId) {
        List<Board> boardsOfUser = boardService.getBoardsByUserId(userId);
        return ResponseEntity.ok(BoardResponse.builder().boards(boardsOfUser).status(Status.SUCCESS).build());
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable String boardId) {
        Board board = boardService.getBoardById(boardId);
        return ResponseEntity.ok(BoardResponse.builder().boards(List.of(board)).status(Status.SUCCESS).build());
    }

    @PostMapping("/{boardId}/lane")
    public ResponseEntity<LaneResponse> createLane(@PathVariable String boardId,
                                                   @Param("name") String name) {
        Lane lane = laneService.createLaneInBoard(boardService.getBoardById(boardId), name);
        return ResponseEntity.ok(LaneResponse.builder().lane(lane).status(Status.SUCCESS).build());
    }

    @DeleteMapping("/{boardId}/lane/{laneId}")
    public ResponseEntity<LaneResponse> deleteLane(@PathVariable String boardId,
                                                   @PathVariable String laneId) {
        laneService.removeLaneFromBoard(boardService.getBoardById(boardId), laneId);
        return ResponseEntity.ok(LaneResponse.builder().status(Status.SUCCESS).build());
    }

    @PostMapping("/{boardId}/lane/{laneId}/card")
    public ResponseEntity<CardResponse> createCard(@PathVariable String boardId,
                                                   @PathVariable String laneId,
                                                   @Param("name") String name) {
        Card card = cardService.createCard(boardService.getBoardById(boardId), laneId, name);
        return ResponseEntity.ok(CardResponse.builder().card(card).status(Status.SUCCESS).build());
    }

    @DeleteMapping("/{boardId}/lane/{laneId}/card/{cardId}")
    public ResponseEntity<CardResponse> deleteCard(@PathVariable String boardId,
                                                   @PathVariable String laneId,
                                                   @PathVariable String cardId) {
        Card card = cardService.removeCard(boardService.getBoardById(boardId), laneId, cardId);
        return ResponseEntity.ok(CardResponse.builder().card(card).status(Status.SUCCESS).build());
    }
}
