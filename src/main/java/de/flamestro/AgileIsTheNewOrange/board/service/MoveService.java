package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.web.model.MoveCardRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
public class MoveService {
    private final BoardService boardService;

    public void moveCardFromSourceToTarget(MoveCardRequest moveCardRequest) {
        Board sourceBoard = boardService.getBoardById(moveCardRequest.getSourceBoardId());
        Lane sourceLane = LaneService.getLaneFromBoard(moveCardRequest.getSourceLaneId(), sourceBoard);
        Card sourceCard = CardService.getCardByIdFromLane(sourceLane, moveCardRequest.getSourceCardId());
        Board targetBoard = boardService.getBoardById(moveCardRequest.getTargetBoardId());
        Lane targetLane = LaneService.getLaneFromBoard(moveCardRequest.getTargetLaneId(), targetBoard);
        Card targetCard = CardService.getCardByIdFromLane(targetLane, moveCardRequest.getTargetCardId());

        Card copyOfSourceCard = copyCardWithNewId(sourceCard);

        addSourceCardToCorrectPosition(targetLane, targetCard, copyOfSourceCard);
        boardService.saveBoard(targetBoard);

        removeCardFromBoard(sourceBoard, sourceLane, sourceCard);
    }

    public Card copyCardWithNewId(Card card) {
        return Card.builder()
                .description(card.getDescription())
                .id(UUID.randomUUID().toString())
                .name(card.getName())
                .build();
    }

    private void addSourceCardToCorrectPosition(Lane lane, Card targetCard, Card sourceCard) {
        if (targetCard != null) {
            addCardToLanePositionedAfterTargetCard(sourceCard, targetCard, lane);
        } else {
            LaneService.appendCardToLane(sourceCard, lane);
        }
    }

    private void addCardToLanePositionedAfterTargetCard(Card card, Card targetCard, Lane targetLane) {
        int targetIndex = targetLane.getCards().indexOf(targetCard);
        targetLane.getCards().add(targetIndex, card);
    }

    private void removeCardFromBoard(Board sourceBoard, Lane sourceLane, Card sourceCard) {
        Board updatedSourceBoard = boardService.getBoardById(sourceBoard.getId());
        Lane laneToRemoveSourceCard = LaneService.getLaneFromBoard(sourceLane.getId(), updatedSourceBoard);
        CardService.removeCardByIdFromLane(sourceCard.getId(), laneToRemoveSourceCard);
        boardService.saveBoard(updatedSourceBoard);
    }
}
