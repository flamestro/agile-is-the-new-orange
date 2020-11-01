package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import de.flamestro.AgileIsTheNewOrange.board.repository.LaneRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@AllArgsConstructor
@Slf4j
public class LaneService {

    private final BoardRepository boardRepository;
    private final LaneRepository laneRepository;

    @Transactional
    public Lane createLane(String name, String boardId){
        Board board = boardRepository.findBoardById(boardId);
        Lane lane = Lane.builder().name(name).cardList(Collections.emptyList()).build();
        laneRepository.save(lane);
        board.getLanes().add(lane);
        boardRepository.save(board);
        log.info("added lane(id={}) to board(id={})", lane.getId(), board.getId());
        return lane;
    }

    @Transactional
    public void removeLane(String laneId, String boardId){
        Lane lane = laneRepository.findLaneById(laneId);
        laneRepository.delete(lane);

        Board board = boardRepository.findBoardById(boardId);
        board.getLanes().removeIf(laneInBoard -> laneInBoard.getId().equals(laneId));
        boardRepository.save(board);

        log.info("removed lane(id={}) from board(id={})", laneId, board.getId());
    }
}
