package de.flamestro.AgileIsTheNewOrange.board.service;

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
        Board board = boardService.createBoard("test_board");

        // do
        Lane lane = laneService.createLane("test_lane", board);

        // then
        assert lane.getId() != null;
        assert lane.getCardList() != null;
        assert !lane.getName().isBlank();
        assertThat(board.getLanes().get(0)).usingRecursiveComparison().isEqualTo(lane);
    }
}