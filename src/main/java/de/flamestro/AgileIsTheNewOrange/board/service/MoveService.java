package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.web.model.MoveCardRequest;

import java.util.UUID;

public class MoveService {

    public static void moveCardFromSourceToTarget(Board sourceBoard, Board targetBoard, MoveCardRequest moveCardRequest) {
        Lane sourceLane = LaneService.getLaneFromBoard(moveCardRequest.getSourceLaneId(), sourceBoard);
        Card sourceCard = CardService.getCardByIdFromLane(moveCardRequest.getSourceCardId(), sourceLane);
        Lane targetLane = LaneService.getLaneFromBoard(moveCardRequest.getTargetLaneId(), targetBoard);
        Card targetCard = CardService.getCardByIdFromLane(moveCardRequest.getTargetCardId(), targetLane);

        Card copyOfSourceCard = copyCardWithNewId(sourceCard);

        CardService.removeCardByIdFromLane(sourceCard.getId(), sourceLane);
        if (sourceBoard.getId().equals(targetBoard.getId())) {
            Lane sourceLaneInTargetBoard = LaneService.getLaneFromBoard(moveCardRequest.getSourceLaneId(), targetBoard);
            CardService.removeCardByIdFromLane(sourceCard.getId(), sourceLaneInTargetBoard);
        }
        addSourceCardToCorrectPosition(targetLane, targetCard, copyOfSourceCard);
    }

    private static Card copyCardWithNewId(Card card) {
        return Card.builder()
                .description(card.getDescription())
                .id(UUID.randomUUID().toString())
                .name(card.getName())
                .build();
    }

    private static void addSourceCardToCorrectPosition(Lane lane, Card targetCard, Card sourceCard) {
        if (targetCard != null) {
            addCardToLaneBeforeTargetCard(sourceCard, targetCard, lane);
        } else {
            LaneService.appendCardToLane(sourceCard, lane);
        }
    }

    private static void addCardToLaneBeforeTargetCard(Card card, Card targetCard, Lane targetLane) {
        int targetIndex = targetLane.getCards().indexOf(targetCard);
        targetLane.getCards().add(targetIndex, card);
    }
}
