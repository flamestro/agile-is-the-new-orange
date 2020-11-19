package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LaneServiceTest extends AbstractIntegrationTest {
    @Autowired
    LaneService laneService;
    @Autowired
    BoardService boardService;

    @Test
    void whenCreateLane_thenReturnLaneSuccessfully() {
        // when
        Board board = boardService.createBoard("test_board", "someUserId");

        // do
        Lane lane = laneService.createLane("test_lane", board);

        // then
        assert lane.getId() != null;
        assert lane.getCards() != null;
        assert !lane.getName().isBlank();
        assertThat(board.getLanes().get(0)).usingRecursiveComparison().isEqualTo(lane);
    }

    @Test
    void whenLaneRemoved_thenBoardDoesNotContainLane() {
        // when
        Board board = boardService.createBoard("test_board", "someUserId");
        Lane lane = laneService.createLane("test_lane", board);

        // do
        assert board.getLanes().get(0).equals(lane);
        laneService.removeLane(lane, board);

        // then
        assert lane.getId() != null;
        assert lane.getCards() != null;
        assert !lane.getName().isBlank();
        assertThat(board.getLanes().size()).isEqualTo(0);
    }
}