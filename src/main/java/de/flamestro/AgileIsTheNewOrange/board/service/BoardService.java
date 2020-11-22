package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
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

@Service
@AllArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final LaneRepository laneRepository;
    private final CardRepository cardRepository;

    @Transactional
    public Board createBoard(String name, String userId){
        Board board = Board.builder().name(name).allowedUsers(new String[]{userId}).lanes(new ArrayList<>()).build();
        boardRepository.save(board);
        log.info("created board with id: {}", board.getId());
        return board;
    }

    @Transactional
    public Board removeBoard(String id){
        Board board = boardRepository.findBoardById(id);
        List<Lane> lanes = board.getLanes();
        for (Lane lane: lanes){
            cardRepository.deleteAll(lane.getCards());
        }
        laneRepository.deleteAll(lanes);
        boardRepository.delete(board);
        return board;
    }

    @Transactional
    public void addLane(Board board, Lane lane){
        board.getLanes().add(lane);
        boardRepository.save(board);
    }

    public List<Board> getBoardByUserId(String userId){
        return boardRepository.findBoardByAllowedUsersContains(userId);
    }

    public Board getBoardById(String id){
        return boardRepository.findBoardById(id);
    }
}
