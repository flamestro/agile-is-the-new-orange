package de.flamestro.AgileIsTheNewOrange.web.model;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class BoardResponse {
    private final Status status;
    private final Board board;
}
