package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.service.BoardService;
import de.flamestro.AgileIsTheNewOrange.web.model.MoveCardRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MoveControllerTest {

    private static final String SOURCE_BOARD_ID = "sourceBoardId";
    private static final String TARGET_BOARD_ID = "targetBoardId";
    private static final String SOURCE_LANE_ID = "sourceLaneId";
    private static final String TARGET_LANE_ID = "targetLaneId";
    private static final String SOURCE_CARD_ID = "sourceCardId";
    private static final String SOURCE_CARD_NAME = "sourceCardName";

    @Mock
    BoardService boardService;

    @InjectMocks
    MoveController moveController;

    @Test
    void whenMoveCardIsCalled_thenBoardsArePersistedAfterwards() {
        List<Card> sourceCards = new ArrayList<>();
        Card sourceCard = Card.builder().id(SOURCE_CARD_ID).name(SOURCE_CARD_NAME).description("cardDescription").build();
        sourceCards.add(sourceCard);
        List<Lane> sourceLanes = new ArrayList<>();
        Lane sourceLane = Lane.builder().cards(sourceCards).name("laneName").id(SOURCE_LANE_ID).build();
        sourceLanes.add(sourceLane);
        Board sourceBoard = Board.builder().lanes(sourceLanes).id(SOURCE_BOARD_ID).allowedUsers(new String[]{"someUser"}).build();

        List<Card> targetCards = new ArrayList<>();
        Lane targetLane = Lane.builder().cards(targetCards).name("laneName").id(TARGET_LANE_ID).build();
        List<Lane> targetLanes = new ArrayList<>();
        targetLanes.add(targetLane);
        Board targetBoard = Board.builder().id(TARGET_BOARD_ID).lanes(targetLanes).allowedUsers(new String[]{"someUser"}).build();

        when(boardService.getBoardById(SOURCE_BOARD_ID)).thenReturn(sourceBoard);
        when(boardService.getBoardById(TARGET_BOARD_ID)).thenReturn(targetBoard);

        moveController.moveCard(MoveCardRequest.builder()
                .sourceBoardId(SOURCE_BOARD_ID)
                .sourceLaneId(SOURCE_LANE_ID)
                .sourceCardId(SOURCE_CARD_ID)
                .targetBoardId(TARGET_BOARD_ID)
                .targetLaneId(TARGET_LANE_ID)
                .targetCardId(null)
                .build());

        verify(boardService).saveBoard(sourceBoard);
        verify(boardService).saveBoard(targetBoard);
    }
}