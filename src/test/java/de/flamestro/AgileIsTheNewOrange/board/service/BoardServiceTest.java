package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.DataProvider;
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
        // do
        String boardName = DataProvider.generateRandomString();
        String userId = DataProvider.generateRandomString();

        Board board = boardService.createBoard(boardName, userId);

        // then
        Board result = mongoTemplate.findById(board.getId(), Board.class);

        assertThat(board.getId()).isNotBlank();
        assertThat(board.getLanes()).hasSize(0);
        assertThat(board.getName()).isNotBlank();
        assertThat(board).usingRecursiveComparison().isEqualTo(result);
    }
}