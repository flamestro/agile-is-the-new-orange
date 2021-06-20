package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;

import java.util.Optional;
import java.util.UUID;

public class CardService {

    public static Card createCard(String name) {
        return buildCardWithName(name);
    }

    private static Card buildCardWithName(String name) {
        return Card.builder().name(name).id(UUID.randomUUID().toString()).build();
    }

    public static void removeCardByIdFromLane(String cardId, Lane lane) {
        lane.getCards().removeIf(cardInLane -> cardInLane.getId().equals(cardId));
    }

    public static Card getCardByIdFromLane(String cardId, Lane lane) {
        Optional<Card> requestedCard = lane.getCards().stream().filter(c -> c.getId().equals(cardId)).findFirst();
        return requestedCard.orElse(null);
    }
}
