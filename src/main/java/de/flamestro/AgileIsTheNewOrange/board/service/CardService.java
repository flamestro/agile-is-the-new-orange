package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CardService {

    private final LaneService laneService;

    public Card createCard(Board board, String laneId, String name) {
        Lane lane = laneService.getLaneByIdFromBoard(board, laneId);
        Card card = buildCardWithName(name);
        laneService.addCard(board, lane, card);
        log.info("added card(id={}) to lane(id={})", card.getId(), lane.getId());
        return card;
    }

    private Card buildCardWithName(String name) {
        return Card.builder().name(name).id(UUID.randomUUID().toString()).build();
    }

    public Card removeCard(Board board, String laneId, String cardId) {
        removeCardByIdFromLane(cardId, laneService.getLaneByIdFromBoard(board, laneId));
        saveBoard(board);
        log.info("removed card(id={}) from lane(id={}) in board(id={})", cardId, laneId, board.getId());
        return null;
    }

    public Card getCardByIdFromLane(Lane lane, String cardId) {
        Optional<Card> requestedCard = lane.getCards().stream().filter(c -> c.getId().equals(cardId)).findFirst();
        return requestedCard.orElse(null);
    }
    public void removeCardByIdFromLane(String cardId, Lane lane) {
        lane.getCards().removeIf(cardInLane -> cardInLane.getId().equals(cardId));
    }

    private void saveBoard(Board board) {
        laneService.saveBoard(board);
    }
}
