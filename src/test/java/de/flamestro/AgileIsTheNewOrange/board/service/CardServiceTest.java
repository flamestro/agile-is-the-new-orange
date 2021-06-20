package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CardServiceTest {

    @Test
    void whenCreateCard_thenCreatedCardIsCorrect() {
        Card card = CardService.createCard("someName");

        assertThat(card.getDescription()).isBlank();
        assertThat(card.getName()).isEqualTo("someName");
    }

    @Test
    void whenRemoveCardByIdFromLane_thenLaneDoesNotContainsCardAnymore() {
        Card card = Card.builder().id("someId").name("someName").build();
        List<Card> cards = new ArrayList<>();
        cards.add(card);
        Lane lane = Lane.builder().cards(cards).build();

        CardService.removeCardByIdFromLane("someId", lane);

        assertThat(lane.getCards()).doesNotContain(card);
    }

    @Test
    void whenGetCardByIdFromLane_thenCorrectCardIsReturned() {
            Card card = Card.builder().id("someId").name("someName").build();
            List<Card> cards = new ArrayList<>();
            cards.add(card);
            Lane lane = Lane.builder().cards(cards).build();

            Card result = CardService.getCardByIdFromLane("someId", lane);

            assertThat(result).usingRecursiveComparison().isEqualTo(card);
    }
}