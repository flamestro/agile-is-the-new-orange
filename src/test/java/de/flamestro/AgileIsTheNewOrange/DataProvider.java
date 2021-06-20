package de.flamestro.AgileIsTheNewOrange;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataProvider {
    public static String generateRandomString() {
        return "test_" + UUID.randomUUID().toString();
    }

    public static void initializeCards(Lane lane){
        if(lane.getCards() == null){
            List<Card> cards = new ArrayList<>();
            lane.setCards(cards);
        }
    }

    public static void addCardToLane(Card card, Lane lane){
        initializeCards(lane);
        lane.getCards().add(card);
    }

    public static void initializeLanes(Board board){
        if(board.getLanes() == null){
            List<Lane> lanes = new ArrayList<>();
            board.setLanes(lanes);
        }
    }

    public static void addLaneToBoard(Lane lane, Board board){
        initializeLanes(board);
        board.getLanes().add(lane);
    }
}
