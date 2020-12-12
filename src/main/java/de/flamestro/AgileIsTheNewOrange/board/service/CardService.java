package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import de.flamestro.AgileIsTheNewOrange.exceptions.InvalidNameException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CardService {

    private final BoardRepository boardRepository;
    private final LaneService laneService;

    public Card createCard(String name, Board board, Lane lane){
        Card card = buildCard(name);
        laneService.addCard(board, lane, card);
        log.info("added card(id={}) to lane(id={})", card.getId(), lane.getId());
        return card;
    }

    public Card removeCard(Board board, Lane lane, Card card){
        List<Lane> singleEntryList = board.getLanes().stream().filter(laneInBoard -> laneInBoard.getId().equals(lane.getId())).collect(Collectors.toList());
        Lane correctLane = singleEntryList.get(0);
        correctLane.getCards().removeIf(cardInLane -> cardInLane.getId().equals(card.getId()));
        boardRepository.save(board);
        log.info("removed card(id={}) from lane(id={}) in board(id={})", card.getId(), lane.getId(), board.getId());
        return card;
    }

    public Card getCardById(String id){
        for (Board board : boardRepository.findAll()){
            for (Lane lane : board.getLanes()){
                for (Card card : lane.getCards()){
                    if(card.getId().equals(id)){
                        return card;
                    }
                }
            }
        }
        //TODO; HANDLE THIS
        return null;
    }

    private Card buildCard(String name) {
        if(name.isBlank()){
            throw new InvalidNameException("Name is blank");
        }
        return Card.builder().name(name).id(UUID.randomUUID().toString()).build();
    }
}
