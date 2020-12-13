package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.web.model.MoveCardRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MoveService {
    private final BoardService boardService;
    private final LaneService laneService;
    private final CardService cardService;

    public void moveCardFromSourceToTarget(MoveCardRequest moveCardRequest) {
        Board sourceBoard = boardService.getBoardById(moveCardRequest.getSourceBoardId());
        Lane sourceLane = laneService.getLaneByIdFromBoard(sourceBoard, moveCardRequest.getSourceLaneId());
        Card sourceCard = cardService.getCardByIdFromLane(sourceLane, moveCardRequest.getSourceCardId());
        Board targetBoard = boardService.getBoardById(moveCardRequest.getTargetBoardId());
        Lane targetLane = laneService.getLaneByIdFromBoard(targetBoard, moveCardRequest.getTargetLaneId());
        Card targetCard = cardService.getCardByIdFromLane(targetLane, moveCardRequest.getTargetCardId());

        Card copyOfSourceCard = copyCardWithNewId(sourceCard);

        addSourceCardToCorrectPosition(targetLane, targetCard, copyOfSourceCard);
        boardService.saveBoard(targetBoard);

        removeCardFromBoard(sourceBoard, sourceLane, sourceCard);
    }

    private void removeCardFromBoard(Board sourceBoard, Lane sourceLane, Card sourceCard) {
        Board updatedSourceBoard = boardService.getBoardById(sourceBoard.getId());
        Lane laneToRemoveSourceCard = laneService.getLaneByIdFromBoard(updatedSourceBoard, sourceLane.getId());
        cardService.removeCardByIdFromLane(sourceCard.getId(), laneToRemoveSourceCard);
        boardService.saveBoard(updatedSourceBoard);
    }

    private void addSourceCardToCorrectPosition(Lane lane, Card targetCard, Card sourceCard) {
        if (targetCard != null) {
            addCardToLanePositionedAfterTargetCard(sourceCard, targetCard, lane);
        } else {
            laneService.appendCardToLane(sourceCard, lane);
        }
    }

    private void addCardToLanePositionedAfterTargetCard(Card card, Card targetCard, Lane targetLane) {
        int targetIndex = targetLane.getCards().indexOf(targetCard);
        targetLane.getCards().add(targetIndex, card);
    }

    public Card copyCardWithNewId(Card card) {
        return Card.builder()
                .description(card.getDescription())
                .id(UUID.randomUUID().toString())
                .name(card.getName())
                .build();
    }
}
