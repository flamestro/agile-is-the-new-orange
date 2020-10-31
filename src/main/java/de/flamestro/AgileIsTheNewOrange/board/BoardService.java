package de.flamestro.AgileIsTheNewOrange.board;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Board createBoard(String name){
        Board board = Board.builder().name(name).lanes(new HashMap<>()).build();
        boardRepository.save(board);
        return board;
    }

    public List<Board> getBoard(String name){
        return boardRepository.findByName(name);
    }
}
