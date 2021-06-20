package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.service.BoardService;
import de.flamestro.AgileIsTheNewOrange.web.model.BoardResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.CardResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.LaneResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardControllerTest {

    private static final String BOARD_NAME = "boardName";
    private static final String LANE_NAME = "laneName";
    private static final String CARD_NAME = "cardName";
    private static final String USER_ID = "userId";
    private static final String BOARD_ID = "boardId";
    private static final String LANE_ID = "laneId";
    private static final String CARD_ID = "cardId";
    @Mock
    BoardService boardService;

    @InjectMocks
    BoardController boardController;

    @Test
    void whenCreateBoard_thenBoardIsCreated() {
        Board board = Board.builder().name(BOARD_NAME).id(BOARD_ID).build();
        when(boardService.createBoard(BOARD_NAME, USER_ID)).thenReturn(board);

        BoardResponse response = boardController.createBoard(BOARD_NAME, USER_ID).getBody();

        assertThat(Objects.requireNonNull(response).getBoards()).contains(board);
    }

    @Test
    void whenDeleteBoard_thenBoardIsDeleted() {
        Board board = Board.builder().name(BOARD_NAME).id(BOARD_ID).build();
        when(boardService.getBoardById(BOARD_ID)).thenReturn(board);
        when(boardService.removeBoard(board)).thenReturn(board);

        BoardResponse response = boardController.deleteBoard(BOARD_ID).getBody();

        assertThat(Objects.requireNonNull(response).getBoards()).contains(board);
    }

    @Test
    void whenGetBoardsByUserId_thenCorrectBoardIsReturned() {
        Board board = Board.builder().name(BOARD_NAME).id(BOARD_ID).allowedUsers(new String[]{USER_ID}).build();
        when(boardService.getBoardsByUserId(USER_ID)).thenReturn(List.of(board));

        BoardResponse response = boardController.getBoardsByUserId(USER_ID).getBody();

        assertThat(Objects.requireNonNull(response).getBoards()).contains(board);
    }

    @Test
    void whenGetBoardsById_thenCorrectBoardIsReturned() {
        Board board = Board.builder().name(BOARD_NAME).id(BOARD_ID).allowedUsers(new String[]{USER_ID}).build();
        when(boardService.getBoardById(BOARD_ID)).thenReturn(board);

        BoardResponse response = boardController.getBoardById(BOARD_ID).getBody();

        assertThat(Objects.requireNonNull(response).getBoards()).contains(board);
    }

    @Test
    void whenCreateLane_thenCorrectLaneIsReturned() {
        Board board = Board.builder().name(BOARD_NAME).id(BOARD_ID).allowedUsers(new String[]{USER_ID}).build();

        when(boardService.getBoardById(BOARD_ID)).thenReturn(board);

        LaneResponse response = boardController.createLane(BOARD_ID, LANE_NAME).getBody();

        assertThat(Objects.requireNonNull(response).getLane().getName()).isEqualTo(LANE_NAME);
        verify(boardService).appendLaneToBoard(any(), any());
    }

    @Test
    void whenDeleteLane_thenCorrectLaneIsReturned() {
        Lane lane = Lane.builder().name(LANE_NAME).id(LANE_ID).build();
        List<Lane> lanes = new ArrayList<>();
        lanes.add(lane);
        Board board = Board.builder().name(BOARD_NAME).id(BOARD_ID).lanes(lanes).allowedUsers(new String[]{USER_ID}).build();

        when(boardService.getBoardById(BOARD_ID)).thenReturn(board);

        LaneResponse response = boardController.deleteLane(BOARD_ID, LANE_ID).getBody();

        assertThat(Objects.requireNonNull(response).getLane().getName()).isEqualTo(LANE_NAME);
        verify(boardService).saveBoard(any());
    }

    @Test
    void whenCreateCard_thenCorrectCardIsReturned() {
        Lane lane = Lane.builder().name(LANE_NAME).id(LANE_ID).build();
        List<Lane> lanes = new ArrayList<>();
        lanes.add(lane);
        Board board = Board.builder().name(BOARD_NAME).id(BOARD_ID).lanes(lanes).allowedUsers(new String[]{USER_ID}).build();

        when(boardService.getBoardById(BOARD_ID)).thenReturn(board);

        CardResponse response = boardController.createCard(BOARD_ID, LANE_ID, CARD_NAME).getBody();

        assertThat(Objects.requireNonNull(response).getCard().getName()).isEqualTo(CARD_NAME);
        assertThat(board.getLanes().get(0).getCards().get(0)).usingRecursiveComparison().isEqualTo(response.getCard());
        verify(boardService).saveBoard(any());
    }

    @Test
    void whenDeleteCard_thenCorrectCardIsReturned() {
        Card card = Card.builder().name(CARD_NAME).id(CARD_ID).build();
        List<Card> cards = new ArrayList<>();
        cards.add(card);

        Lane lane = Lane.builder().name(LANE_NAME).cards(cards).id(LANE_ID).build();
        List<Lane> lanes = new ArrayList<>();
        lanes.add(lane);
        Board board = Board.builder().name(BOARD_NAME).id(BOARD_ID).lanes(lanes).allowedUsers(new String[]{USER_ID}).build();

        when(boardService.getBoardById(BOARD_ID)).thenReturn(board);

        CardResponse response = boardController.deleteCard(BOARD_ID, LANE_ID, CARD_ID).getBody();

        assertThat(Objects.requireNonNull(response).getCard().getName()).isEqualTo(CARD_NAME);
        assertThat(board.getLanes().get(0).getCards()).isEmpty();
        verify(boardService).saveBoard(board);
    }
}