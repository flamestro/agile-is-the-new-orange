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
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final LaneService laneService;
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@Param("name") String name){
        Board board = boardService.createBoard(name);
        return ResponseEntity.ok(BoardResponse.builder().board(board).status(Status.SUCCESS).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoardById(@PathVariable String id){
        Board board = boardService.getBoardById(id);
        return ResponseEntity.ok(BoardResponse.builder().board(board).status(Status.SUCCESS).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BoardResponse> removeBoardById(@PathVariable String id){
        Board board = boardService.removeBoard(id);
        return ResponseEntity.ok(BoardResponse.builder().board(board).status(Status.SUCCESS).build());
    }

    @PostMapping("/{boardId}/lane")
    public ResponseEntity<LaneResponse> addLaneToBoard(@PathVariable String boardId, @Param("name") String name){
        Lane lane = laneService.createLane(name, boardService.getBoardById(boardId));
        return ResponseEntity.ok(LaneResponse.builder().lane(lane).status(Status.SUCCESS).build());
    }

    @DeleteMapping("/{boardId}/lane/{laneId}")
    public ResponseEntity<LaneResponse> removeLaneFromBoard(@PathVariable String boardId, @PathVariable String laneId){
        Lane lane = laneService.removeLane(laneService.getLaneById(laneId), boardService.getBoardById(boardId));
        return ResponseEntity.ok(LaneResponse.builder().lane(lane).status(Status.SUCCESS).build());
    }

    @PostMapping("/{boardId}/lane/{laneId}/card")
    public ResponseEntity<CardResponse> addCardToLane(@PathVariable String boardId, @PathVariable String laneId, @Param("name") String name){
        Card card = cardService.createCard(name, boardService.getBoardById(boardId), laneService.getLaneById(laneId));
        return ResponseEntity.ok(CardResponse.builder().card(card).status(Status.SUCCESS).build());
    }

    @DeleteMapping("/{boardId}/lane/{laneId}/card/{cardId}")
    public ResponseEntity<CardResponse> removeCardFromLane(@PathVariable String boardId, @PathVariable String laneId, @PathVariable String cardId){
        Card card = cardService.removeCard(boardService.getBoardById(boardId), laneService.getLaneById(laneId), cardService.getCardById(cardId));
        return ResponseEntity.ok(CardResponse.builder().card(card).status(Status.SUCCESS).build());
    }
}
