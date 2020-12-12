package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.exceptions.InvalidNameException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CardService {

    private final LaneService laneService;

    public Card createCard(Board board, String laneId, String name) {
        Lane lane = laneService.getLaneFromBoard(board, laneId);
        Card card = buildCard(name);
        laneService.addCard(board, lane, card);
        log.info("added card(id={}) to lane(id={})", card.getId(), lane.getId());
        return card;
    }

    public Card removeCard(Board board, String laneId, String cardId) {
        laneService.removeCardFromLane(cardId, laneService.getLaneFromBoard(board, laneId));
        saveBoard(board);
        log.info("removed card(id={}) from lane(id={}) in board(id={})", cardId, laneId, board.getId());
        return null;
    }

    private Card buildCard(String name) {
        if (name.isBlank()) {
            throw new InvalidNameException("Name is blank");
        }
        return Card.builder().name(name).id(UUID.randomUUID().toString()).build();
    }

    private void saveBoard(Board board) {
        laneService.saveBoard(board);
    }
}
