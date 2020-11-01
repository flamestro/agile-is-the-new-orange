package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(String name){
        Board board = Board.builder().name(name).lanes(new HashMap<>()).build();
        boardRepository.save(board);
        log.info("created board with id: {}", board.getId());
        return board;
    }

    public List<Board> getBoardByName(String name){
        return boardRepository.findByName(name);
    }

    public Board getBoardById(String id){
        return boardRepository.findBoardById(id);
    }

    public void removeBoard(String id){
        Board board = boardRepository.findBoardById(id);
        boardRepository.delete(board);
    }

}
