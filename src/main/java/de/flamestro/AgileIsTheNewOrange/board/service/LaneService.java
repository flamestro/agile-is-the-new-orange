package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
@Slf4j
public class LaneService {

    private final BoardRepository boardRepository;

    public Board createLane(String name, String boardId){
        Board board = boardRepository.findBoardById(boardId);
        Lane lane = Lane.builder().name(name).cardList(Collections.emptyList()).build();
        board.getLanes().add(lane);
        log.info("added lane(id={}) to board(id={})", lane.getId(), board.getId());
        return board;
    }

    public void removeLane(String laneId, String boardId){
        Board board = boardRepository.findBoardById(boardId);

        board.getLanes().removeIf(lane -> lane.getId().equals(laneId));
        log.info("removed lane(id={}) to board(id={})", laneId, board.getId());
    }
}
