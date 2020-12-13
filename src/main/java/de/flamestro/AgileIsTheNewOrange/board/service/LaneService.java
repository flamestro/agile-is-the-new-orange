package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.exceptions.InvalidNameException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class LaneService {

    private final BoardService boardService;

    public Lane createLaneInBoard(Board board, String name) {
        Lane lane = buildLaneWithValidatedName(name);
        boardService.addLaneToBoard(board, lane);
        log.info("added lane(id={}) to board(id={})", lane.getId(), board.getId());
        return lane;
    }

    public void removeLaneFromBoard(Board board, String laneId) {
        deleteLaneByIdFromBoard(laneId, board);
        saveBoard(board);
    }

    public void addCard(Board board, Lane lane, Card card) {
        appendCardToLane(card, lane);
        saveBoard(board);
    }

    public void appendCardToLane(Card card, Lane lane) {
        lane.getCards()
                .add(card);
    }

    public Lane getLaneByIdFromBoard(Board board, String laneId) {
        Optional<Lane> requestedLane = board.getLanes()
                .stream()
                .filter(laneInBoard -> laneInBoard.getId().equals(laneId))
                .findFirst();
        if (requestedLane.isPresent()) {
            return requestedLane.get();
        } else {
            throw new RuntimeException("Requested Lane was not found in Board");
        }
    }

    private void deleteLaneByIdFromBoard(String laneId, Board board) {
        board.getLanes().removeIf(laneInBoard -> laneInBoard.getId().equals(laneId));
    }

    private Lane buildLaneWithValidatedName(String name) {
        validateName(name);
        return buildLaneWithName(name);
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new InvalidNameException("Name is blank");
        }
    }

    private Lane buildLaneWithName(String name) {
        return Lane.builder()
                .name(name)
                .id(UUID.randomUUID().toString())
                .cards(new ArrayList<>())
                .build();
    }

    public void saveBoard(Board board) {
        boardService.saveBoard(board);
    }
}
