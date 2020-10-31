package de.flamestro.AgileIsTheNewOrange.board;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.util.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class BoardServiceTest extends AbstractIntegrationTest {
    @Autowired
    BoardService boardService;

    @Test
    void whenCreateBoard_thenBoardIsInMongoDB(@Autowired MongoTemplate mongoTemplate) {
        // when
        Board board = boardService.createBoard("test_board");

        Board result = mongoTemplate.findById(board.getId(), Board.class);

        assertThat(board).usingRecursiveComparison().isEqualTo(result);
    }

}