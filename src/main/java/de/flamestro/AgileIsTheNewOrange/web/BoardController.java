package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class BoardController {

    @GetMapping("/board")
    public static ResponseEntity<Board> getBoard(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/board")
    public static ResponseEntity<Board> addBoard(){
        return ResponseEntity.ok().build();
    }
}
