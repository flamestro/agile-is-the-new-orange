package de.flamestro.AgileIsTheNewOrange.board.repository;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepository extends MongoRepository<Board, String> {
    Board findBoardById(String id);
}
