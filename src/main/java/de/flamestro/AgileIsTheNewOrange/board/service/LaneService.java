package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.exceptions.InvalidNameException;
import de.flamestro.AgileIsTheNewOrange.web.model.MoveCardRequest;
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
        Lane lane = buildLaneWithName(name);
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

    public void moveCardFromSourceToTarget(MoveCardRequest moveCardRequest) {
        Board sourceBoard = boardService.getBoardById(moveCardRequest.getSourceBoardId());
        Lane sourceLane = getLaneByIdFromBoard(sourceBoard, moveCardRequest.getSourceLaneId());
        Card sourceCard = getCardByIdFromLane(sourceLane, moveCardRequest.getSourceCardId());
        Board targetBoard = boardService.getBoardById(moveCardRequest.getTargetBoardId());
        Lane targetLane = getLaneByIdFromBoard(targetBoard, moveCardRequest.getTargetLaneId());
        Card targetCard = getCardByIdFromLane(targetLane, moveCardRequest.getTargetCardId());

        Card copyOfSourceCard = sourceCard.copyWithNewId();

        addSourceCardToCorrectPosition(targetLane, targetCard, copyOfSourceCard);
        saveBoard(targetBoard);

        removeCardFromBoard(sourceBoard, sourceLane, sourceCard);
    }

    private void removeCardFromBoard(Board sourceBoard, Lane sourceLane, Card sourceCard) {
        Board updatedSourceBoard = boardService.getBoardById(sourceBoard.getId());
        Lane laneToRemoveSourceCard = getLaneByIdFromBoard(updatedSourceBoard, sourceLane.getId());
        removeCardByIdFromLane(sourceCard.getId(), laneToRemoveSourceCard);
        saveBoard(updatedSourceBoard);
    }

    private void addSourceCardToCorrectPosition(Lane lane, Card targetCard, Card sourceCard) {
        if (targetCard != null) {
            addCardToLanePositionedAfterTargetCard(sourceCard, targetCard, lane);
        } else {
            appendCardToLane(sourceCard, lane);
        }
    }

    private void appendCardToLane(Card card, Lane lane) {
        lane.getCards()
                .add(card);
    }

    public void removeCardByIdFromLane(String cardId, Lane lane) {
        lane.getCards().removeIf(cardInLane -> cardInLane.getId().equals(cardId));
    }

    private void addCardToLanePositionedAfterTargetCard(Card card, Card targetCard, Lane targetLane) {
        int targetIndex = targetLane.getCards().indexOf(targetCard);
        targetLane.getCards().add(targetIndex, card);
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

    private Card getCardByIdFromLane(Lane lane, String cardId) {
        Optional<Card> requestedCard = lane.getCards().stream().filter(c -> c.getId().equals(cardId)).findFirst();
        return requestedCard.orElse(null);
    }

    private void deleteLaneByIdFromBoard(String laneId, Board board) {
        board.getLanes().removeIf(laneInBoard -> laneInBoard.getId().equals(laneId));
    }

    private Lane buildLaneWithName(String name) {
        if (name.isBlank()) {
            throw new InvalidNameException("Name is blank");
        }
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
