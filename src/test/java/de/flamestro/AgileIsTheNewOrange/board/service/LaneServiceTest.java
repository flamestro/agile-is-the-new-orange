package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.DataProvider;
import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class LaneServiceTest extends AbstractIntegrationTest {
    @Autowired
    LaneService laneService;
    @Autowired
    BoardService boardService;

    @Test
    void whenCreateLane_thenReturnLaneSuccessfully() {
        // when
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();
        String laneName = DataProvider.generateRandomString();

        Board board = boardService.createBoard(boardName, userId);

        // do
        Lane lane = laneService.createLaneInBoard(board, laneName);

        // then
        assertThat(lane.getId()).isNotBlank();
        assertThat(lane.getCards()).hasSize(0);
        assertThat(lane.getName()).isNotBlank();
        assertThat(board.getLanes().get(0)).usingRecursiveComparison().isEqualTo(lane);
    }

    @Test
    void whenLaneRemoved_thenBoardDoesNotContainLane() {
        // when
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();
        String laneName = DataProvider.generateRandomString();

        Board board = boardService.createBoard(boardName, userId);
        Lane lane = laneService.createLaneInBoard(board, laneName);

        // do
        assertThat(board.getLanes().get(0)).isEqualTo(lane);
        laneService.removeLaneFromBoard(board, lane.getId());

        // then
        assertThat(lane.getId()).isNotBlank();
        assertThat(lane.getCards()).hasSize(0);
        assertThat(lane.getName()).isNotBlank();
        assertThat(board.getLanes().size()).isEqualTo(0);
    }
}