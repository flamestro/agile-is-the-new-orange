package de.flamestro.AgileIsTheNewOrange.board.repository;

import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface LaneRepository extends MongoRepository<Lane, String> {
    Lane findLaneById(String id);
}
