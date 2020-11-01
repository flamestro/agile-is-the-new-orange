package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.util.AbstractIntegrationTest;
import de.flamestro.AgileIsTheNewOrange.web.model.BoardResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BoardControllerTest extends AbstractIntegrationTest {

    @Autowired
    BoardController boardController;

    @Test
    void whenGetBoardIsCalled_thenReturnValidResponse() {
        // when
        BoardResponse expectedBoard = boardController.createBoard("Mock").getBody();
        assert expectedBoard != null;

        // do
        ResponseEntity<BoardResponse> result = boardController.getBoardById(expectedBoard.getBoard().getId());

        // then
        BoardResponse responseBody = result.getBody();
        assert responseBody != null;
        assertThat(responseBody.getBoard()).usingRecursiveComparison().isEqualTo(expectedBoard.getBoard());
        assertThat(responseBody.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void whenAddBoardIsCalled_thenReturnValidResponse() {
        // do
        ResponseEntity<BoardResponse> responseEntity = boardController.createBoard("Mock");

        // then
        BoardResponse result = responseEntity.getBody();
        assert result != null;
        assertThat(result.getBoard().getName()).isEqualTo("Mock");
        assertThat(result.getBoard().getLanes()).isEqualTo(Collections.emptyList());
        assertThat(result.getBoard().getId()).isNotBlank();
        assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
    }
}