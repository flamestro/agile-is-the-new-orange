package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import de.flamestro.AgileIsTheNewOrange.exceptions.InvalidNameException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(String name, String userId){
        Board board = buildBoard(name, userId);
        boardRepository.save(board);
        log.info("created board with id: {}", board.getId());
        return board;
    }

    private Board buildBoard(String name, String userId) {
        if(name.isBlank()){
            throw new InvalidNameException("Name is blank");
        }
        return Board.builder().name(name).allowedUsers(new String[]{userId}).lanes(new ArrayList<>()).build();
    }

    public Board removeBoard(Board board){
        boardRepository.delete(board);
        return board;
    }

    public void addLaneToBoard(Board board, Lane lane){
        board.getLanes().add(lane);
        boardRepository.save(board);
    }

    public List<Board> getBoardsByUserId(String userId){
        return boardRepository.findBoardByAllowedUsersContains(userId);
    }

    public Board getBoardById(String id){
        return boardRepository.findBoardById(id);
    }
}
