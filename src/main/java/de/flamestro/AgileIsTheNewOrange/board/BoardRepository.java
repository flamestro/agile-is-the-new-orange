package de.flamestro.AgileIsTheNewOrange.board;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BoardRepository extends MongoRepository<Board, String> {
    List<Board> findByName(String name);
}