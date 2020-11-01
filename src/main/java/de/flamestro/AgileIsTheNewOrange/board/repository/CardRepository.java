package de.flamestro.AgileIsTheNewOrange.board.repository;

import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CardRepository extends MongoRepository<Card, String> {
    Card findCardById(String id);
}
