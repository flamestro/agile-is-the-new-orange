package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.Board;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class BoardControllerTest {

    @Test
    void whenGetBoardIsCalled_thenReturnValidResponse() {
        Board expectedBoard = Board.builder().name("Mock Board").build();

        ResponseEntity<Board> responseEntity = BoardController.getBoard();

        assertThat(responseEntity.getBody()).usingRecursiveComparison().isEqualTo(expectedBoard);
    }

    @Test
    void whenAddBoardIsCalled_thenReturnValidResponse() {
        Board expectedBoard = Board.builder().name("Mock Board").build();

        ResponseEntity<Board> responseEntity = BoardController.addBoard();

        assertThat(responseEntity.getBody()).usingRecursiveComparison().isEqualTo(expectedBoard);
    }
}