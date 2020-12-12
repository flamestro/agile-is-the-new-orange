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
        Lane lane = buildLane(name);
        boardService.addLaneToBoard(board, lane);
        log.info("added lane(id={}) to board(id={})", lane.getId(), board.getId());
        return lane;
    }

    public void removeLaneFromBoard(Board board, String laneId) {
        deleteLaneFromBoard(laneId, board);
        saveBoard(board);
    }

    public void addCard(Board board, Lane lane, Card card) {
        addCardToLane(card, lane);
        saveBoard(board);
    }

    public void moveCard(MoveCardRequest moveCardRequest) {
        Board sourceBoard = boardService.getBoardById(moveCardRequest.getSourceBoardId());
        Lane sourceLane = getLaneFromBoard(sourceBoard, moveCardRequest.getSourceLaneId());
        Card sourceCard = getRequestedCardByIdFormLane(moveCardRequest.getSourceCardId(), sourceLane);
        Board targetBoard = boardService.getBoardById(moveCardRequest.getTargetBoardId());
        Lane targetLane = getLaneFromBoard(targetBoard, moveCardRequest.getTargetLaneId());
        Card targetCard = getRequestedCardByIdFormLane(moveCardRequest.getTargetCardId(), targetLane);

        Card copyOfSourceCard = sourceCard.copyWithNewId();

        addCardToCorrectPosition(targetCard, copyOfSourceCard, targetLane);
        saveBoard(targetBoard);

        removeCardFromBoard(sourceCard, sourceLane, sourceBoard);
    }

    private void removeCardFromBoard(Card sourceCard, Lane sourceLane, Board sourceBoard) {
        Board updatedSourceBoard = boardService.getBoardById(sourceBoard.getId());
        Lane laneToRemoveSourceCard = getLaneFromBoard(updatedSourceBoard, sourceLane.getId());
        removeCardFromLane(sourceCard.getId(), laneToRemoveSourceCard);
        saveBoard(updatedSourceBoard);
    }

    private void addCardToCorrectPosition(Card targetCard, Card sourceCard, Lane lane) {
        if (targetCard != null) {
            addCardAfterTargetCard(sourceCard, targetCard, lane);
        } else {
            addCardToLane(sourceCard, lane);
        }
    }

    public void removeCardFromLane(String sourceCardId, Lane laneToRemoveSourceCard) {
        laneToRemoveSourceCard.getCards().removeIf(cardInLane -> cardInLane.getId().equals(sourceCardId));
    }

    private void addCardAfterTargetCard(Card card, Card otherCard, Lane targetLane) {
        Card requestedCardFormLane = getRequestedCardByIdFormLane(otherCard.getId(), targetLane);
        int targetIndex = targetLane.getCards().indexOf(requestedCardFormLane);
        targetLane.getCards().add(targetIndex, card);
    }

    public void saveBoard(Board board) {
        boardService.saveBoard(board);
    }

    private void addCardToLane(Card card, Lane lane) {
        lane.getCards()
                .add(card);
    }

    public Lane getLaneFromBoard(Board board, String laneId) {
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

    private Card getRequestedCardByIdFormLane(String targetCardId, Lane lane) {
        Optional<Card> requestedCard = lane.getCards().stream().filter(c -> c.getId().equals(targetCardId)).findFirst();
        return requestedCard.orElse(null);
    }

    private void deleteLaneFromBoard(String laneId, Board board) {
        board.getLanes().removeIf(laneInBoard -> laneInBoard.getId().equals(laneId));
    }

    private Lane buildLane(String name) {
        if (name.isBlank()) {
            throw new InvalidNameException("Name is blank");
        }
        return Lane.builder()
                .name(name)
                .id(UUID.randomUUID().toString())
                .cards(new ArrayList<>())
                .build();
    }
}
