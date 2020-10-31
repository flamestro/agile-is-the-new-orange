package de.flamestro.AgileIsTheNewOrange.board;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.HashMap;

@Getter
@Builder
@ToString
public class Board {
    @Id
    public String id;

    private final String name;
    private final HashMap<Integer, Lane> lanes;
}
