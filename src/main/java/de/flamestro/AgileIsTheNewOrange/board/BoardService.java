package de.flamestro.AgileIsTheNewOrange.board;

import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(String name){
        Board board = Board.builder().name(name).build();
        boardRepository.save(board);
        return board;
    }

    public List<Board> getBoardById(String name){
        return boardRepository.findByName(name);
    }
}
