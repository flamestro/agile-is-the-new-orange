package de.flamestro.AgileIsTheNewOrange.board;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Builder
@ToString
public class Board {
    private final String name;
    private final HashMap<Integer, Lane> lanes;
}
