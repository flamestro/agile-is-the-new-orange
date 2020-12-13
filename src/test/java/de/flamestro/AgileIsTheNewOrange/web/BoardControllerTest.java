package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.DataProvider;
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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardControllerTest extends AbstractIntegrationTest {

    @Autowired
    BoardController boardController;

    @Test
    void whenGetBoardIsCalled_thenReturnValidResponse() {
        // when
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();
        BoardResponse expectedBoard = boardController.createBoard(boardName, userId).getBody();
        assertThat(expectedBoard).isNotNull();

        // do
        ResponseEntity<BoardResponse> result = boardController.getBoardById(expectedBoard.getBoards().get(0).getId());

        // then
        BoardResponse responseBody = result.getBody();
        assertThat(responseBody).isNotNull();
        assertThat(responseBody.getBoards().get(0)).usingRecursiveComparison().isEqualTo(expectedBoard.getBoards().get(0));
        assertThat(responseBody.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void whenAddBoardIsCalled_thenReturnValidResponse() {
        // do
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();
        ResponseEntity<BoardResponse> responseEntity = boardController.createBoard(boardName, userId);

        // then
        BoardResponse result = responseEntity.getBody();

        assertThat(result).isNotNull();
        assertThat(result.getBoards().get(0).getName()).isEqualTo(boardName);
        assertThat(result.getBoards().get(0).getLanes()).isEqualTo(Collections.emptyList());
        assertThat(result.getBoards().get(0).getId()).isNotBlank();
        assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void whenRemoveBoardById_thenResponseIsTheSameBoard() {
        // when
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();
        Board board = createMockBoard(boardName, userId);

        // do
        ResponseEntity<BoardResponse> removeResponseEntity = boardController.deleteBoard(board.getId());

        // then
        BoardResponse result = removeResponseEntity.getBody();

        assertThat(result).isNotNull();
        assertThat(result.getBoards().get(0).getName()).isEqualTo(boardName);
        assertThat(result.getBoards().get(0).getLanes()).isEqualTo(Collections.emptyList());
        assertThat(result.getBoards().get(0).getId()).isNotBlank();
        assertThat(result.getStatus()).isEqualTo(Status.SUCCESS);
        assertThat(result.getBoards().get(0)).usingRecursiveComparison().isEqualTo(board);
    }

    @Test
    void whenLaneIsAddedToBoard_thenBoardContainsLane(@Autowired MongoTemplate mongoTemplate) {
        // when
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();
        Board board = createMockBoard(boardName, userId);

        // do
        ResponseEntity<LaneResponse> laneResponseResponseEntity = boardController.createLane(board.getId(), "TestLane");

        // then
        Board resultBoard = mongoTemplate.findById(board.getId(), Board.class);
        assertThat(resultBoard).isNotNull();
        LaneResponse resultLaneResponse = laneResponseResponseEntity.getBody();
        assert resultLaneResponse != null;
        assertThat(resultLaneResponse).isNotNull();

        assertThat(resultBoard.getName()).isEqualTo(boardName);
        assertThat(resultBoard.getLanes().size()).isEqualTo(1);
        assertThat(resultBoard.getLanes().get(0)).usingRecursiveComparison().isEqualTo(resultLaneResponse.getLane());
        assertThat(resultBoard.getId()).isEqualTo(board.getId());
        assertThat(resultLaneResponse.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void whenLaneIsRemovedFromBoard_thenBoardLanesEmpty(@Autowired MongoTemplate mongoTemplate) {
        // when
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();
        Board board = createMockBoard(boardName, userId);

        // do
        ResponseEntity<LaneResponse> laneResponseResponseEntity = boardController.createLane(board.getId(), "TestLane");

        // then
        Board resultBoard = mongoTemplate.findById(board.getId(), Board.class);
        assertThat(resultBoard).isNotNull();
        LaneResponse resultLaneResponse = laneResponseResponseEntity.getBody();
        assertThat(resultLaneResponse).isNotNull();

        assertThat(resultBoard.getLanes().size()).isEqualTo(1);
        assertThat(resultBoard.getLanes().get(0)).usingRecursiveComparison().isEqualTo(resultLaneResponse.getLane());

        // do
        boardController.deleteLane(board.getId(), resultLaneResponse.getLane().getId());

        // then
        assertThat(board.getLanes()).isEmpty();
    }

    @Test
    void whenCardIsAddedToLane_thenLaneContainsCard(@Autowired MongoTemplate mongoTemplate) {
        // when
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();
        Board board = createMockBoard(boardName, userId);

        LaneResponse lane = boardController.createLane(board.getId(), "TestLane").getBody();
        assertThat(lane).isNotNull();

        // do
        CardResponse cardResponseResponseEntity = boardController.createCard(board.getId(), lane.getLane().getId(), "TestCard").getBody();
        assertThat(cardResponseResponseEntity).isNotNull();

        // then
        Card resultCard = mongoTemplate.findById(board.getId(), Board.class).getLanes().get(0).getCards().get(0);
        assertThat(resultCard).isNotNull();

        assertThat(resultCard.getName()).isEqualTo("TestCard");
        assertThat(resultCard.getDescription()).isEqualTo(null);
        assertThat(cardResponseResponseEntity.getStatus()).isEqualTo(Status.SUCCESS);
    }

    @Test
    void whenCardIsRemovedFromLane_thenLaneHasNoCards(@Autowired MongoTemplate mongoTemplate) {
        // when
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();
        Board board = createMockBoard(boardName, userId);

        LaneResponse lane = boardController.createLane(board.getId(), "TestLane").getBody();
        assertThat(lane).isNotNull();
        CardResponse cardResponse = boardController.createCard(board.getId(), lane.getLane().getId(), "TestLane").getBody();
        assertThat(cardResponse).isNotNull();

        //do
        boardController.deleteCard(board.getId(), lane.getLane().getId(), cardResponse.getCard().getId());

        // then
        List<Card> resultCards = mongoTemplate.findById(board.getId(), Board.class).getLanes().get(0).getCards();
        assertThat(resultCards).isEmpty();
    }

    private Board createMockBoard(String boardName, String userId){
        // when
        ResponseEntity<BoardResponse> responseEntity = boardController.createBoard(boardName, userId);
        BoardResponse createResponse = responseEntity.getBody();
        assertThat(createResponse).isNotNull();

        Board board = createResponse.getBoards().get(0);
        assertThat(board).isNotNull();
        assertThat(board.getLanes()).isNotNull();
        assertThat(board.getLanes()).hasSize(0);
        assertThat(board.getName()).isNotBlank();

        return board;
    }
}