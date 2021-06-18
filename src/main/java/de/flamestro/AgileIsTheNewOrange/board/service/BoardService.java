package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(String name, String userId) {
        Board board = buildBoardWithName(name, userId);
        saveBoard(board);
        log.info("created board with id: {}", board.getId());
        return board;
    }

    private Board buildBoardWithName(String name, String userId) {
        return Board.builder().name(name).allowedUsers(new String[]{userId}).lanes(new ArrayList<>()).build();
    }

    public void saveBoard(Board board) {
        boardRepository.save(board);
    }

    public Board removeBoard(Board board) {
        boardRepository.delete(board);
        return board;
    }

    public void appendLaneToBoard(Board board, Lane lane) {
        if(board.getLanes() == null){
            List<Lane> lanes = new ArrayList<>();
            board.setLanes(lanes);
        }
        board.getLanes().add(lane);
        saveBoard(board);
    }

    public List<Board> getBoardsByUserId(String userId) {
        return boardRepository.findBoardByAllowedUsersContains(new String[]{userId});
    }

    public Board getBoardById(String id) {
        return boardRepository.findBoardById(id);
    }
}
