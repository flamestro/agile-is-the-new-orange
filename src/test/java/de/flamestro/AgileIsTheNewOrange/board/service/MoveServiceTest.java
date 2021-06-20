package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.web.model.MoveCardRequest;
import org.junit.jupiter.api.Test;

import static de.flamestro.AgileIsTheNewOrange.DataProvider.*;
import static org.assertj.core.api.Assertions.assertThat;

class MoveServiceTest {

    private static final String SOURCE_BOARD_ID = "sourceBoardId";
    private static final String TARGET_BOARD_ID = "targetBoardId";
    private static final String SOURCE_LANE_ID = "sourceLaneId";
    private static final String TARGET_LANE_ID = "targetLaneId";
    private static final String SOURCE_CARD_ID = "sourceCardId";
    private static final String TARGET_CARD_ID = "targetCardId";
    private static final String SOURCE_CARD_NAME = "sourceCardName";
    private static final String TARGET_CARD_NAME = "targetCardName";

    @Test
    void whenBoardIsDifferent_thenMoveCorrectly() {
        Card sourceCard = Card.builder().id(SOURCE_CARD_ID).name(SOURCE_CARD_NAME).description("cardDescription").build();
        Lane sourceLane = Lane.builder().name("laneName").id(SOURCE_LANE_ID).build();
        Board sourceBoard = Board.builder().id(SOURCE_BOARD_ID).allowedUsers(new String[]{"someUser"}).build();
        addCardToLane(sourceCard, sourceLane);
        addLaneToBoard(sourceLane, sourceBoard);

        Lane targetLane = Lane.builder().name("laneName").id(TARGET_LANE_ID).build();
        Board targetBoard = Board.builder().id(TARGET_BOARD_ID).allowedUsers(new String[]{"someUser"}).build();
        initializeCards(targetLane);
        addLaneToBoard(targetLane, targetBoard);

        MoveService.moveCardFromSourceToTarget(sourceBoard, targetBoard, MoveCardRequest.builder()
                .sourceBoardId(SOURCE_BOARD_ID)
                .sourceLaneId(SOURCE_LANE_ID)
                .sourceCardId(SOURCE_CARD_ID)
                .targetBoardId(TARGET_BOARD_ID)
                .targetLaneId(TARGET_LANE_ID)
                .targetCardId(null)
                .build());

        assertThat(sourceLane.getCards()).isEmpty();
        assertThat(targetLane.getCards()).hasSize(1);
        assertThat(targetLane.getCards().get(0).getName()).isEqualTo(SOURCE_CARD_NAME);
        assertThat(sourceBoard.getLanes()).hasSize(1);
        assertThat(sourceBoard.getLanes()).contains(sourceLane);
        assertThat(targetBoard.getLanes()).hasSize(1);
        assertThat(targetBoard.getLanes()).contains(targetLane);
    }

    @Test
    void whenBoardIsDifferent_andSpecifiesTargetCard_thenMoveCorrectly() {
        Card sourceCard = Card.builder().id(SOURCE_CARD_ID).name(SOURCE_CARD_NAME).description("cardDescription").build();
        Lane sourceLane = Lane.builder().name("laneName").id(SOURCE_LANE_ID).build();
        Board sourceBoard = Board.builder().id(SOURCE_BOARD_ID).allowedUsers(new String[]{"someUser"}).build();
        addCardToLane(sourceCard, sourceLane);
        addLaneToBoard(sourceLane, sourceBoard);

        Card targetCard = Card.builder().id(TARGET_CARD_ID).name(TARGET_CARD_NAME).description("cardDescription").build();
        Lane targetLane = Lane.builder().name("laneName").id(TARGET_LANE_ID).build();
        Board targetBoard = Board.builder().id(TARGET_BOARD_ID).allowedUsers(new String[]{"someUser"}).build();
        addCardToLane(targetCard, targetLane);
        addLaneToBoard(targetLane, targetBoard);

        MoveService.moveCardFromSourceToTarget(sourceBoard, targetBoard, MoveCardRequest.builder()
                .sourceBoardId(SOURCE_BOARD_ID)
                .sourceLaneId(SOURCE_LANE_ID)
                .sourceCardId(SOURCE_CARD_ID)
                .targetBoardId(TARGET_BOARD_ID)
                .targetLaneId(TARGET_LANE_ID)
                .targetCardId(TARGET_CARD_ID)
                .build());

        assertThat(sourceLane.getCards()).isEmpty();
        assertThat(targetLane.getCards()).hasSize(2);
        assertThat(targetLane.getCards().get(0).getName()).isEqualTo(SOURCE_CARD_NAME);
        assertThat(targetLane.getCards().get(1).getName()).isEqualTo(TARGET_CARD_NAME);
        assertThat(sourceBoard.getLanes()).hasSize(1);
        assertThat(sourceBoard.getLanes()).contains(sourceLane);
        assertThat(targetBoard.getLanes()).hasSize(1);
        assertThat(targetBoard.getLanes()).contains(targetLane);
    }


    @Test
    void whenBoardIsSame_thenMoveCorrectly() {
        Card sourceCard = Card.builder().id(SOURCE_CARD_ID).name(SOURCE_CARD_NAME).description("cardDescription").build();
        Lane sourceLane = Lane.builder().name("lane1Name").id(SOURCE_LANE_ID).build();
        Lane targetLane = Lane.builder().name("lane2Name").id(TARGET_LANE_ID).build();
        Board sourceBoard = Board.builder().id(SOURCE_BOARD_ID).allowedUsers(new String[]{"someUser"}).build();
        addCardToLane(sourceCard, sourceLane);
        initializeCards(targetLane);
        addLaneToBoard(sourceLane, sourceBoard);
        addLaneToBoard(targetLane, sourceBoard);

        MoveService.moveCardFromSourceToTarget(sourceBoard, sourceBoard, MoveCardRequest.builder()
                .sourceBoardId(SOURCE_BOARD_ID)
                .sourceLaneId(SOURCE_LANE_ID)
                .sourceCardId(SOURCE_CARD_ID)
                .targetBoardId(SOURCE_BOARD_ID)
                .targetLaneId(TARGET_LANE_ID)
                .targetCardId(null)
                .build());

        assertThat(sourceLane.getCards()).isEmpty();
        assertThat(targetLane.getCards()).hasSize(1);
        assertThat(targetLane.getCards().get(0).getName()).isEqualTo(SOURCE_CARD_NAME);
        assertThat(sourceBoard.getLanes()).hasSize(2);
        assertThat(sourceBoard.getLanes()).contains(sourceLane);
        assertThat(sourceBoard.getLanes()).contains(targetLane);
    }

    @Test
    void whenBoardIsSame_andSpecifiesTargetCard_thenMoveCorrectly() {
        Card sourceCard = Card.builder().id(SOURCE_CARD_ID).name(SOURCE_CARD_NAME).description("cardDescription").build();
        Card targetCard = Card.builder().id(TARGET_CARD_ID).name(TARGET_CARD_NAME).description("cardDescription").build();
        Lane sourceLane = Lane.builder().name("lane1Name").id(SOURCE_LANE_ID).build();
        Lane targetLane = Lane.builder().name("lane2Name").id(TARGET_LANE_ID).build();
        Board sourceBoard = Board.builder().id(SOURCE_BOARD_ID).allowedUsers(new String[]{"someUser"}).build();
        addCardToLane(sourceCard, sourceLane);
        addCardToLane(targetCard, targetLane);
        addLaneToBoard(sourceLane, sourceBoard);
        addLaneToBoard(targetLane, sourceBoard);

        MoveService.moveCardFromSourceToTarget(sourceBoard, sourceBoard, MoveCardRequest.builder()
                .sourceBoardId(SOURCE_BOARD_ID)
                .sourceLaneId(SOURCE_LANE_ID)
                .sourceCardId(SOURCE_CARD_ID)
                .targetBoardId(SOURCE_BOARD_ID)
                .targetLaneId(TARGET_LANE_ID)
                .targetCardId(TARGET_CARD_ID)
                .build());

        assertThat(sourceLane.getCards()).isEmpty();
        assertThat(targetLane.getCards()).hasSize(2);
        assertThat(targetLane.getCards().get(0).getName()).isEqualTo(SOURCE_CARD_NAME);
        assertThat(targetLane.getCards().get(1).getName()).isEqualTo(TARGET_CARD_NAME);
        assertThat(sourceBoard.getLanes()).hasSize(2);
        assertThat(sourceBoard.getLanes()).contains(sourceLane);
        assertThat(sourceBoard.getLanes()).contains(targetLane);
    }
}