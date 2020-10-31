package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.util.AbstractIntegrationTest;
import de.flamestro.AgileIsTheNewOrange.board.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BoardControllerTest extends AbstractIntegrationTest {

    @Autowired
    BoardController boardController;

    @Test
    void whenGetBoardIsCalled_thenReturnValidResponse() {
        // when
        var expectedBoard = boardController.createBoard("Mock").getBody();
        assert expectedBoard != null;
        var expectedBoardList = List.of(expectedBoard);

        // do
        ResponseEntity<List<Board>> result = boardController.getBoard("Mock");
        var resultBoardList = result.getBody();

        // then
        assert resultBoardList != null;
        assertThat(resultBoardList.get(0)).usingRecursiveComparison().isEqualTo(expectedBoardList.get(0));
    }

    @Test
    void whenAddBoardIsCalled_thenReturnValidResponse() {
        // do
        ResponseEntity<Board> responseEntity = boardController.createBoard("Mock");

        // then
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getName()).isEqualTo("Mock");
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getLanes()).isEqualTo(new HashMap<>());
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getId()).isNotBlank();
    }
}