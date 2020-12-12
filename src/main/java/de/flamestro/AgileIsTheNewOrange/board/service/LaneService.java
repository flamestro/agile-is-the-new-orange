package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
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

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    public Lane createLaneInBoard(String name, Board board) {
        Lane lane = buildLane(name);
        boardService.addLaneToBoard(board, lane);
        log.info("added lane(id={}) to board(id={})", lane.getId(), board.getId());
        return lane;
    }

    public Lane removeLaneFromBoard(Lane lane, Board board) {
        deleteLaneFromBoard(lane, board);
        saveBoard(board);
        return lane;
    }

    public void addCard(Board board, Lane lane, Card card) {
        Lane laneToAddCard = getRequestedLaneFromBoard(board, lane);
        addCardToLane(card, laneToAddCard);
        saveBoard(board);
    }

    public void moveCard(Card sourceCard, Lane sourceLane, Board sourceBoard, Card targetCard, Lane targetLane, Board targetBoard) {
        Card copyOfSourceCard = sourceCard.copyWithNewId();
        Lane lane = getRequestedLaneFromBoard(targetBoard, targetLane);

        addCardToCorrectPosition(targetCard, copyOfSourceCard, lane);
        saveBoard(targetBoard);

        Board updatedSourceBoard = boardRepository.findBoardById(sourceBoard.getId());
        Lane laneToRemoveSourceCard = getRequestedLaneFromBoard(updatedSourceBoard, sourceLane);
        removeCardFromLane(sourceCard, laneToRemoveSourceCard);
        saveBoard(updatedSourceBoard);
    }

    public Lane getLaneById(String id) {
        for (Board board : boardRepository.findAll()) {
            for (Lane lane : board.getLanes()) {
                if (lane.getId().equals(id)) {
                    return lane;
                }
            }
        }
        //TODO: HANDLE THIS
        return null;
    }

    private void addCardToCorrectPosition(Card targetCard, Card sourceCard, Lane lane) {
        if (targetCard != null) {
            addCardAfterTargetCard(sourceCard, targetCard, lane);
        } else {
            addCardToLane(sourceCard, lane);
        }
    }

    private void removeCardFromLane(Card sourceCard, Lane laneToRemoveSourceCard) {
        laneToRemoveSourceCard.getCards().removeIf(cardInLane -> cardInLane.getId().equals(sourceCard.getId()));
    }

    private void addCardAfterTargetCard(Card card, Card otherCard, Lane targetLane) {
        Card requestedCardFormLane = getRequestedCardFormLane(otherCard, targetLane);
        int targetIndex = targetLane.getCards().indexOf(requestedCardFormLane);
        targetLane.getCards().add(targetIndex, card);
    }

    private void saveBoard(Board board) {
        boardRepository.save(board);
    }

    private void addCardToLane(Card card, Lane lane) {
        lane.getCards()
                .add(card);
    }

    private Lane getRequestedLaneFromBoard(Board board, Lane lane) {
        Optional<Lane> requestedLane = board.getLanes()
                .stream()
                .filter(laneInBoard -> laneInBoard.getId().equals(lane.getId()))
                .findFirst();
        if (requestedLane.isPresent()) {
            return requestedLane.get();
        } else {
            throw new RuntimeException("Requested Lane was not found in Board");
        }
    }

    private Card getRequestedCardFormLane(Card targetCard, Lane lane) {
        Optional<Card> requestedCard = lane.getCards().stream().filter(c -> c.getId().equals(targetCard.getId())).findFirst();
        if (requestedCard.isPresent()) {
            return requestedCard.get();
        } else {
            throw new RuntimeException("Requested Lane was not found in Board");
        }
    }


    private void deleteLaneFromBoard(Lane lane, Board board) {
        board.getLanes().removeIf(laneInBoard -> laneInBoard.getId().equals(lane.getId()));
    }

    private Lane buildLane(String name) {
        if(name.isBlank()){
            throw new InvalidNameException("Name is blank");
        }
        return Lane.builder()
                .name(name)
                .id(UUID.randomUUID().toString())
                .cards(new ArrayList<>())
                .build();
    }
}
