package de.flamestro.AgileIsTheNewOrange.board;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

@Getter
@Builder
public class Board {
    private final String name;
    private final HashMap<Integer, Lane> lanes;
}
