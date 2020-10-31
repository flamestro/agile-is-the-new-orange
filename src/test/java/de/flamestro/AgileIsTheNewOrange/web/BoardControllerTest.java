package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardControllerTest {

    @Autowired
    BoardController boardController;

    @Test
    void whenGetBoardIsCalled_thenReturnValidResponse() {
        Board expectedBoard = Board.builder().name("Mock Board").build();

        ResponseEntity<List<Board>> responseEntity = boardController.getBoard("Mock");

        assertThat(responseEntity.getBody()).usingRecursiveComparison().isEqualTo(expectedBoard);
    }

    @Test
    void whenAddBoardIsCalled_thenReturnValidResponse() {
        Board expectedBoard = Board.builder().name("Mock Board").build();

        ResponseEntity<Board> responseEntity = boardController.createBoard("Mock");

        assertThat(responseEntity.getBody()).usingRecursiveComparison().isEqualTo(expectedBoard);
    }
}