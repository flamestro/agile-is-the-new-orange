package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.DataProvider;
import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    BoardService boardService;

    @Test
    void whenCreateBoard_thenReturnedBoardIsCorrect() {
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();

        Board board = boardService.createBoard(boardName, userId);

        assertThat(board.getLanes()).hasSize(0);
        assertThat(board.getName()).isNotBlank();
        assertThat(board.getAllowedUsers()).contains(userId);
        verify(boardRepository).save(board);
    }

    @Test
    void whenRemoveBoard_thenBoardRepositoryIsCalledWithBoard() {
        Board board = boardService.removeBoard(Board.builder().build());

        verify(boardRepository).delete(board);
    }

    @Test
    void whenAppendLaneToBoard_thenLaneIsInBoard() {
        Lane lane = Lane.builder().id("laneId").build();
        Board board = Board.builder().id("boardId").build();

        boardService.appendLaneToBoard(board, lane);

        assertThat(board.getLanes()).contains(lane);
        verify(boardRepository).save(board);
    }

    @Test
    void whenGetBoardByUserId_thenBoardRepositoryIsCalled() {
        boardService.getBoardsByUserId("someId");

        verify(boardRepository).findBoardByAllowedUsersContains(new String[]{"someId"});
    }

    @Test
    void whenGetBoardById_thenBoardRepositoryIsCalled() {
        boardService.getBoardById("someId");

        verify(boardRepository).findBoardById("someId");
    }
}