package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LaneService {

    public static Lane createLane(String name) {
        return Lane.builder()
                .name(name)
                .id(UUID.randomUUID().toString())
                .cards(new ArrayList<>())
                .build();
    }

    public static void removeLaneFromBoard(String laneId, Board board) {
        board.getLanes().removeIf(laneInBoard -> laneInBoard.getId().equals(laneId));
    }

    public static void appendCardToLane(Card card, Lane lane) {
        if (lane.getCards() == null) {
            List<Card> cards = new ArrayList<>();
            lane.setCards(cards);
        }
        lane.getCards().add(card);
    }

    public static Lane getLaneFromBoard(String laneId, Board board) {
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
}
