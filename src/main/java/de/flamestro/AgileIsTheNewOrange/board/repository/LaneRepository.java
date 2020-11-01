package de.flamestro.AgileIsTheNewOrange.board.repository;

import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LaneRepository extends MongoRepository<Lane, String> {
    List<Lane> findByName(String name);
    Lane findBoardById(String id);
}
