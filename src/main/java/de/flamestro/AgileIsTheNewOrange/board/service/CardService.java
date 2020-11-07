package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import de.flamestro.AgileIsTheNewOrange.board.repository.CardRepository;
import de.flamestro.AgileIsTheNewOrange.board.repository.LaneRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CardService {

    private final BoardRepository boardRepository;
    private final LaneRepository laneRepository;
    private final CardRepository cardRepository;
    private final LaneService laneService;

    @Transactional
    public Card createCard(String name, Board board, Lane lane){
        Card card = Card.builder().name(name).build();
        cardRepository.save(card);
        laneService.addCard(board, lane, card);
        log.info("added card(id={}) to lane(id={})", card.getId(), lane.getId());
        return card;
    }

    @Transactional
    public Card removeCard(Board board, Lane lane, Card card){
        cardRepository.delete(card);
        List<Lane> singleEntryList = board.getLanes().stream().filter(laneInBoard -> laneInBoard.getId().equals(lane.getId())).collect(Collectors.toList());
        Lane correctLane = singleEntryList.get(0);
        correctLane.getCards().removeIf(cardInLane -> cardInLane.getId().equals(card.getId()));
        laneRepository.save(lane);
        boardRepository.save(board);
        log.info("removed card(id={}) from lane(id={}) in board(id={})", card.getId(), lane.getId(), board.getId());
        return card;
    }

    public Card getCardById(String id){
        return cardRepository.findCardById(id);
    }
}
