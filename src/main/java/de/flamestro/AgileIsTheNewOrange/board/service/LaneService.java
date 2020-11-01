package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import de.flamestro.AgileIsTheNewOrange.board.repository.LaneRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
@AllArgsConstructor
@Slf4j
public class LaneService {

    private final LaneRepository laneRepository;
    private final BoardRepository boardRepository;
    private final BoardService boardService;

    @Transactional
    public Lane createLane(String name, Board board){
        Lane lane = Lane.builder().name(name).cardList(new ArrayList<>()).build();
        laneRepository.save(lane);
        boardService.addLane(board, lane);
        log.info("added lane(id={}) to board(id={})", lane.getId(), board.getId());
        return lane;
    }

    @Transactional
    public Lane removeLane(Lane lane, Board board){
        laneRepository.delete(lane);
        board.getLanes().removeIf(laneInBoard -> laneInBoard.getId().equals(lane.getId()));
        boardRepository.save(board);
        log.info("removed lane(id={}) from board(id={})", lane.getId(), board.getId());
        return lane;
    }

    @Transactional
    public void addCard(Lane lane, Card card){
        lane.getCardList().add(card);
        laneRepository.save(lane);
    }

    public Lane getLaneById(String id){
        return laneRepository.findLaneById(id);
    }
}
