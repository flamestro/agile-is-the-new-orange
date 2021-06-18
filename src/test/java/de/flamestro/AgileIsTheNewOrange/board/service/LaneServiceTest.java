package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LaneServiceTest {

    @Test
    void whenCreateLane_thenReturnLaneSuccessfully() {
        Lane lane = LaneService.createLane("someName");

        assertThat(lane.getId()).isNotBlank();
        assertThat(lane.getCards()).hasSize(0);
        assertThat(lane.getName()).isNotBlank();
    }

    @Test
    void whenLaneRemoved_thenBoardDoesNotContainLane() {
        List<Lane> lanes = new ArrayList<>();
        lanes.add(Lane.builder().id("someId").build());
        Board board = Board.builder().lanes(lanes).build();

        LaneService.removeLaneFromBoard("someId", board);

        assertThat(board.getLanes()).hasSize(0);
    }

    @Test
    void whenAppendCardToLane_thenCardIsInLane() {
        Lane lane = Lane.builder().id("someId").build();
        Card card = Card.builder().id("cardId").build();
        LaneService.appendCardToLane(card, lane);

        assertThat(lane.getCards()).contains(card);
    }

    @Test
    void whenGetLaneFromBoard_thenReturnCorrectLane() {
        Lane lane = Lane.builder().id("someId").build();
        Board board = Board.builder().lanes(List.of(lane)).build();
        Lane requestedLane = LaneService.getLaneFromBoard("someId", board);

        assertThat(requestedLane).isEqualTo(lane);
    }

    @Test
    void whenGetLaneFromBoard_andBoardDoesNotContainLane_thenReturnCorrectLane() {
        Board board = Board.builder().lanes(Collections.emptyList()).build();

        Throwable throwable = assertThrows(RuntimeException.class, () -> {
            LaneService.getLaneFromBoard("someId", board);
        });

        assertThat(throwable.getMessage()).isEqualTo("Requested Lane was not found in Board");
    }
}