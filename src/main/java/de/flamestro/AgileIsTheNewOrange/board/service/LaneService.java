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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LaneService {

    private final LaneRepository laneRepository;
    private final BoardRepository boardRepository;
    private final CardRepository cardRepository;
    private final BoardService boardService;

    @Transactional
    public Lane createLane(String name, Board board) {
        Lane lane = Lane.builder().name(name).cards(new ArrayList<>()).build();
        laneRepository.save(lane);
        boardService.addLane(board, lane);
        log.info("added lane(id={}) to board(id={})", lane.getId(), board.getId());
        return lane;
    }

    @Transactional
    public Lane removeLane(Lane lane, Board board) {
        cardRepository.deleteAll(lane.getCards());
        laneRepository.delete(lane);
        board.getLanes().removeIf(laneInBoard -> laneInBoard.getId().equals(lane.getId()));
        boardRepository.save(board);
        log.info("removed lane(id={}) from board(id={})", lane.getId(), board.getId());
        return lane;
    }

    @Transactional
    public void addCard(Board board, Lane lane, Card card) {
        List<Lane> laneList = board.getLanes()
                .stream()
                .filter(laneInBoard -> laneInBoard.getId().equals(lane.getId()))
                .collect(Collectors.toList());
        laneList.get(0).getCards().add(card);
        boardRepository.save(board);
    }

    @Transactional
    public void moveCard(Card sourceCard, Lane sourceLane, Board sourceBoard, String targetCard, Lane targetLane, Board targetBoard) {

        Card copyOfSourceCard = Card.builder().description(sourceCard.getDescription()).name(sourceCard.getName()).build();
        cardRepository.save(copyOfSourceCard);

        List<Lane> laneList = targetBoard.getLanes()
                .stream()
                .filter(laneInBoard -> laneInBoard.getId().equals(targetLane.getId()))
                .collect(Collectors.toList());
        Card targetInList = laneList.get(0).getCards().stream().filter(c -> c.getId().equals(targetCard)).findAny().get();
        int index = laneList.get(0).getCards().indexOf(targetInList);
        laneList.get(0).getCards().add(index, copyOfSourceCard);
        boardRepository.save(targetBoard);

        Board newSource = boardRepository.findBoardById(sourceBoard.getId());
        List<Lane> singleEntryList = newSource.getLanes().stream().filter(laneInBoard -> laneInBoard.getId().equals(sourceLane.getId())).collect(Collectors.toList());
        Lane correctLane = singleEntryList.get(0);
        correctLane.getCards().removeIf(cardInLane -> cardInLane.getId().equals(sourceCard.getId()));
        laneRepository.save(sourceLane);
        boardRepository.save(sourceBoard);
        boardRepository.save(newSource);
    }

    public Lane getLaneById(String id) {
        return laneRepository.findLaneById(id);
    }
}
