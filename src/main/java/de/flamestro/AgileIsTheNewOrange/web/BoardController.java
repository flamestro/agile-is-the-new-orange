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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/board")
@AllArgsConstructor
@Slf4j
@Validated
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@Valid @NotBlank @Param("name") String name,
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
                                                   @Valid @NotBlank @Param("name") String name) {
        Board board = boardService.getBoardById(boardId);
        Lane lane = LaneService.createLane(name);
        boardService.appendLaneToBoard(board, lane);

        log.info("added lane(id={}) to board(id={})", lane.getId(), board.getId());
        return ResponseEntity.ok(LaneResponse.builder().lane(lane).status(Status.SUCCESS).build());
    }

    @DeleteMapping("/{boardId}/lane/{laneId}")
    public ResponseEntity<LaneResponse> deleteLane(@PathVariable String boardId,
                                                   @PathVariable String laneId) {
        Board board = boardService.getBoardById(boardId);
        Lane lane = LaneService.getLaneFromBoard(laneId, board);
        LaneService.removeLaneFromBoard(laneId, board);
        boardService.saveBoard(board);
        return ResponseEntity.ok(LaneResponse.builder().lane(lane).status(Status.SUCCESS).build());
    }

    @PostMapping("/{boardId}/lane/{laneId}/card")
    public ResponseEntity<CardResponse> createCard(@PathVariable String boardId,
                                                   @PathVariable String laneId,
                                                   @Valid @NotBlank @Param("name") String name) {
        Board board = boardService.getBoardById(boardId);
        Lane lane = LaneService.getLaneFromBoard(laneId, board);
        Card card = CardService.createCard(name);
        LaneService.appendCardToLane(card, lane);
        boardService.saveBoard(board);
        log.info("added card(id={}) to lane(id={})", card.getId(), lane.getId());
        return ResponseEntity.ok(CardResponse.builder().card(card).status(Status.SUCCESS).build());
    }

    @DeleteMapping("/{boardId}/lane/{laneId}/card/{cardId}")
    public ResponseEntity<CardResponse> deleteCard(@PathVariable String boardId,
                                                   @PathVariable String laneId,
                                                   @PathVariable String cardId) {
        Board board = boardService.getBoardById(boardId);
        Lane lane = LaneService.getLaneFromBoard(laneId, board);
        Card card = CardService.getCardByIdFromLane(lane, cardId);
        CardService.removeCardByIdFromLane(cardId, LaneService.getLaneFromBoard(laneId, board));
        boardService.saveBoard(board);
        log.info("removed card(id={}) from lane(id={}) in board(id={})", cardId, laneId, board.getId());
        return ResponseEntity.ok(CardResponse.builder().card(card).status(Status.SUCCESS).build());
    }
}
