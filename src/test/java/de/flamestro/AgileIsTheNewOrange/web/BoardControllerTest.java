package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.util.AbstractIntegrationTest;
import de.flamestro.AgileIsTheNewOrange.web.model.BoardResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.CardResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.LaneResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BoardControllerTest extends AbstractIntegrationTest {

    @Autowired
    BoardController boardController;

    @Test
    void whenGetBoardIsCalled_thenReturnValidResponse() {
        // when
        BoardResponse expectedBoard = boardController.createBoard("Mock", "someUser").getBody();
        assert expectedBoard != null;

        // do
        ResponseEntity<BoardResponse> result = boardController.getBoardById(expectedBoard.getBoards().get(0).getId());

        // then
        BoardResponse responseBody = result.getBody();
        assert responseBody != null;
        assertThat(responseBody.getBoards().get(0)).usingRecursiveComparison().isEqualTo(expectedBoard.getBoards().get(0));
        assertThat(responseBody.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void whenAddBoardIsCalled_thenReturnValidResponse() {
        // do
        ResponseEntity<BoardResponse> responseEntity = boardController.createBoard("Mock", "someUser");

        // then
        BoardResponse result = responseEntity.getBody();
        assert result != null;
        assertThat(result.getBoards().get(0).getName()).isEqualTo("Mock");
        assertThat(result.getBoards().get(0).getLanes()).isEqualTo(Collections.emptyList());
        assertThat(result.getBoards().get(0).getId()).isNotBlank();
        assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void whenRemoveBoardById_thenResponseIsTheSameBoard() {
        // when
        Board board = createMockBoard();

        // do
        ResponseEntity<BoardResponse> removeResponseEntity = boardController.removeBoardById(board.getId());

        // then
        BoardResponse result = removeResponseEntity.getBody();
        assert result != null;
        assertThat(result.getBoards().get(0).getName()).isEqualTo("Mock");
        assertThat(result.getBoards().get(0).getLanes()).isEqualTo(Collections.emptyList());
        assertThat(result.getBoards().get(0).getId()).isNotBlank();
        assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
        assertThat(result.getBoards().get(0)).usingRecursiveComparison().isEqualTo(board);
    }

    @Test
    void whenLaneIsAddedToBoard_thenBoardContainsLane(@Autowired MongoTemplate mongoTemplate) {
        // when
        Board board = createMockBoard();

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
        Board board = createMockBoard();

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

    @Test
    void whenCardIsAddedToLane_thenLaneContainsCard(@Autowired MongoTemplate mongoTemplate) {
        // when
        Board board = createMockBoard();
        LaneResponse lane = boardController.addLaneToBoard(board.getId(), "TestLane").getBody();
        assert lane != null;

        // do
        CardResponse cardResponseResponseEntity = boardController.addCardToLane(board.getId(), lane.getLane().getId(), "TestLane").getBody();
        assert cardResponseResponseEntity != null;

        // then
        Card resultCard = mongoTemplate.findById(board.getId(), Board.class).getLanes().get(0).getCards().get(0);
        assert resultCard != null;

        assertThat(resultCard.getName()).isEqualTo("TestLane");
        assertThat(resultCard.getDescription()).isEqualTo(null);
        assertThat(cardResponseResponseEntity.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void whenCardIsRemovedFromLane_thenLaneHasNoCards(@Autowired MongoTemplate mongoTemplate) {
        // when
        Board board = createMockBoard();
        LaneResponse lane = boardController.addLaneToBoard(board.getId(), "TestLane").getBody();
        assert lane != null;
        CardResponse cardResponse = boardController.addCardToLane(board.getId(), lane.getLane().getId(), "TestLane").getBody();
        assert cardResponse != null;

        //do
        boardController.removeCardFromLane(board.getId(), lane.getLane().getId(), cardResponse.getCard().getId());

        // then
        List<Card> resultCards = mongoTemplate.findById(board.getId(), Board.class).getLanes().get(0).getCards();
        assertThat(resultCards).isEmpty();
    }

    private Board createMockBoard(){
        // when
        ResponseEntity<BoardResponse> responseEntity = boardController.createBoard("Mock", "someUser");
        BoardResponse createResponse = responseEntity.getBody();
        assert createResponse != null;

        Board board = createResponse.getBoards().get(0);
        assert board != null;
        assert board.getLanes() != null;
        assert board.getLanes().size() == 0;
        assert !board.getName().isBlank();

        return board;
    }
}