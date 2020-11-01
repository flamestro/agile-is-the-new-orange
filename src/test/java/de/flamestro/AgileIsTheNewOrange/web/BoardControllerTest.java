package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.util.AbstractIntegrationTest;
import de.flamestro.AgileIsTheNewOrange.web.model.BoardResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.LaneResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

    @Test
    void whenRemoveBoardById_thenResponseIsTheSameBoard() {
        // when
        Board board = createBoard();

        // do
        ResponseEntity<BoardResponse> removeResponseEntity = boardController.removeBoardById(board.getId());

        // then
        BoardResponse result = removeResponseEntity.getBody();
        assert result != null;
        assertThat(result.getBoard().getName()).isEqualTo("Mock");
        assertThat(result.getBoard().getLanes()).isEqualTo(Collections.emptyList());
        assertThat(result.getBoard().getId()).isNotBlank();
        assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
        assertThat(result.getBoard()).usingRecursiveComparison().isEqualTo(board);
    }

    @Test
    void whenLaneIsAddedToBoard_thenBoardContainsLane(@Autowired MongoTemplate mongoTemplate) {
        // when
        Board board = createBoard();

        // do
        ResponseEntity<LaneResponse> laneResponseResponseEntity = boardController.addLaneToBoard(board.getId(), "TestLane");

        // then
        Board resultBoard = mongoTemplate.findById(board.getId(), Board.class);
        assert resultBoard != null;
        LaneResponse resultLaneResponse = laneResponseResponseEntity.getBody();
        assert resultLaneResponse != null;

        assertThat(resultBoard.getName()).isEqualTo("Mock");
        assertThat(resultBoard.getLanes().size()).isEqualTo(1);
        assertThat(resultBoard.getLanes().get(0)).usingRecursiveComparison().isEqualTo(resultLaneResponse.getLane());
        assertThat(resultBoard.getId()).isEqualTo(board.getId());
        assertThat(resultLaneResponse.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void whenLaneIsRemovedFromBoard_thenBoardLanesEmpty(@Autowired MongoTemplate mongoTemplate) {
        // when
        Board board = createBoard();

        // do
        ResponseEntity<LaneResponse> laneResponseResponseEntity = boardController.addLaneToBoard(board.getId(), "TestLane");

        // then
        Board resultBoard = mongoTemplate.findById(board.getId(), Board.class);
        assert resultBoard != null;
        LaneResponse resultLaneResponse = laneResponseResponseEntity.getBody();
        assert resultLaneResponse != null;

        assertThat(resultBoard.getLanes().size()).isEqualTo(1);
        assertThat(resultBoard.getLanes().get(0)).usingRecursiveComparison().isEqualTo(resultLaneResponse.getLane());

        // do
        boardController.removeLaneFromBoard(board.getId(), resultLaneResponse.getLane().getId());

        // then
        assertThat(board.getLanes()).isEmpty();
    }

    private Board createBoard(){
        // when
        ResponseEntity<BoardResponse> responseEntity = boardController.createBoard("Mock");
        BoardResponse createResponse = responseEntity.getBody();
        assert createResponse != null;

        Board board = createResponse.getBoard();
        assert board != null;
        assert board.getLanes() != null;
        assert board.getLanes().size() == 0;
        assert !board.getName().isBlank();

        return board;
    }
}